package com.ecommerce.controller;

import com.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {
    private final PaymentService paymentService;


    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigheader) {
        paymentService.handleStripeEvent(payload,sigheader);
        return ResponseEntity.ok("Recieved");
    }
}
