package com.ecommerce.service;

import com.ecommerce.dto.payment.CheckoutResponseDto;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.model.order.Order;
import com.ecommerce.model.order.OrderItem;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.order.OrderRepository;
import com.ecommerce.util.AuthenticateClient;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public CheckoutResponseDto createCheckoutSession(Long orderId) {

        try {

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found"));

            if (order.getOrderItems().isEmpty()) {
                throw new IllegalStateException("Cannot create a stripe session for an empty order");
            }

//        Map Order Items to Stripe LongItems
            List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

            for (OrderItem orderItem : order.getOrderItems()) {
                lineItems.add(createLineItem(orderItem));
            }

//        Create Stripe Session
            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:5173")
                    .setCancelUrl("http://localhost:5173/koszyk")
                    .addAllLineItem(lineItems)
                    .setClientReferenceId(String.valueOf(orderId))
                    .build();

            Session session = Session.create(params);

            System.out.println("ClientReferenceId from Stripe: " + session.getClientReferenceId());


            return CheckoutResponseDto.builder()
                    .orderNumber(order.getOrderNumber())
                    .stripeCheckoutUrl(session.getUrl())
                    .build();

        } catch (com.stripe.exception.StripeException e) {
            throw new RuntimeException("Stripe error: " + e.getMessage(), e);
        }
    }


    private SessionCreateParams.LineItem createLineItem(OrderItem orderItem) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity((long) orderItem.getQuantity())
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("pln")
                        .setUnitAmount((long) (orderItem.getPriceAtPurchase().doubleValue() * 100))
                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(orderItem.getPlant().getName())
                                .build())
                        .build())
                .build();
    }

    private void handleCHeckSessionCompleted(Event event) {

        Session session = (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElse(null);

        if (session == null) return;

        Long orderId = Long.valueOf(session.getClientReferenceId());
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new IllegalStateException("Order not found"));

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        System.out.println("Order " + orderId + " marked as PAID");
    }


    public void handleStripeEvent (String payload, String sigHeader) {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        }
        catch (Exception e) {
            throw new RuntimeException("Webhook signature verification failed", e);
        }

        switch (event.getType()) {
            case "checkout.session.completed":
                handleCHeckSessionCompleted(event);
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }
    }
}