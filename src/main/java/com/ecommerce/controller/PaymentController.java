package com.ecommerce.controller;

import com.ecommerce.dto.payment.CheckoutRequestDto;
import com.ecommerce.dto.payment.CheckoutResponseDto;
import com.ecommerce.model.user.Client;
import com.ecommerce.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponseDto> createCheckoutSession(@Valid @RequestBody CheckoutRequestDto checkoutRequestDto) {
        CheckoutResponseDto checkoutResponseDto = paymentService.createCheckoutSession(checkoutRequestDto.getOrderId());
        return ResponseEntity.ok(checkoutResponseDto);
    }
}