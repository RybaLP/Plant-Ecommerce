package com.ecommerce.controller;

import com.ecommerce.dto.payment.CheckoutRequestDto;
import com.ecommerce.dto.payment.CheckoutResponseDto;
import com.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/stripe/session")
    public CheckoutResponseDto createStripeSession (@RequestBody CheckoutRequestDto checkoutRequestDto) {
        return paymentService.createCheckoutSession(checkoutRequestDto.getOrderId());
    }

}