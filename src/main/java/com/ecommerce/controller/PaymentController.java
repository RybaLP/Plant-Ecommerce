package com.ecommerce.controller;

import com.ecommerce.dto.payment.CheckoutRequestDto;
import com.ecommerce.dto.payment.CheckoutResponseDto;
import com.ecommerce.model.user.Client;
import com.ecommerce.service.PaymentService;
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

    @PostMapping("/create-checkout-session")
    public ResponseEntity<CheckoutResponseDto> createCheckoutSession(
            @RequestBody CheckoutRequestDto checkoutRequestDto,
            @AuthenticationPrincipal Client client
    ) throws Exception {
        CheckoutResponseDto checkoutResponseDto = paymentService.createCheckoutSession(
                checkoutRequestDto.getOrderId(), client
        );

        return new ResponseEntity<>(checkoutResponseDto, HttpStatus.OK);
    }


}
