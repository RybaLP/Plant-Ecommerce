package com.ecommerce.service;

import com.ecommerce.dto.payment.CheckoutRequestDto;
import com.ecommerce.dto.payment.CheckoutResponseDto;
import com.ecommerce.model.order.Order;
import com.ecommerce.model.order.OrderItem;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.order.OrderRepository;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;

    public CheckoutResponseDto createCheckoutSession (Long orderId, Client client) throws Exception {

        Order order = orderRepository.findById(orderId)
                .filter(o -> o.getClient().equals(client))
                .orElseThrow(()->new IllegalArgumentException("Order not found"));

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
                .setSuccessUrl("https://example.com/success")
                .setCancelUrl("https://example.com/success")
                .addAllLineItem(lineItems)
                .build();

        Session session = Session.create(params);

        return CheckoutResponseDto.builder()
                .stripeCheckoutUrl(session.getUrl())
                .build();

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

}
