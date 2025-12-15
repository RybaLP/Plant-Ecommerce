package com.ecommerce.service;

import com.ecommerce.dto.payment.CheckoutResponseDto;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.model.order.Order;
import com.ecommerce.model.order.OrderItem;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.order.OrderRepository;
import com.ecommerce.util.AuthenticateClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Value("${frontendUrl}")
    private String frontendUrl;


    public CheckoutResponseDto createCheckoutSession(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException(""));

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(frontendUrl + "/zamowienie?orderNumber=" + order.getOrderNumber())
                    .setCancelUrl(frontendUrl + "/")
                    .putMetadata("orderNumber", order.getOrderNumber())
                    .addAllLineItem(createLineItems(order))
                    .build();

            Session session = Session.create(params);

            return CheckoutResponseDto.builder()
                    .orderNumber(order.getOrderNumber())
                    .stripeCheckoutUrl(session.getUrl())
                    .build();

        } catch (StripeException ex) {
            throw new RuntimeException("" + ex);
        }
    }


    private List<SessionCreateParams.LineItem> createLineItems(Order order) {
        return order.getOrderItems().stream()
                .map(this::createLineItem)
                .collect(Collectors.toList());
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem orderItem) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity((long) orderItem.getQuantity())
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("pln")
                        .setUnitAmount(orderItem.getPriceAtPurchase()
                                .multiply(BigDecimal.valueOf(100))
                                .longValue())
                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(orderItem.getPlant().getName())
                                .build())
                        .build())
                .build();
    }


// cases

    public void handleStripeWebHook (String payload, String sigHeader) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (StripeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        switch (event.getType()) {
            case "checkout.session.completed":
                handleCheckoutSessionCompleted(event);
                break;
            case "checkout.session.expired":
                break;

            case "payment_intent.payment_failed":
            handleFailedPayment(event);
            break;
            default:
                break;
        }
    }



    private void handleCheckoutSessionCompleted(Event event) {
        Session session = (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElse(null);

        if (session == null) return;

        String orderNumber = session.getMetadata().get("orderNumber");
        if (orderNumber == null) return;

        Order order = orderRepository.findByOrderNumber(orderNumber);
        if (order != null) {
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
        }
    }

    public void handleFailedPayment (Event event) {
        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
        if (session == null) return;

        String orderNumber = session.getMetadata().get("orderNumber");
        if (orderNumber == null) return;

        Order order = orderRepository.findByOrderNumber(orderNumber);

        if (order != null) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }
    }

}