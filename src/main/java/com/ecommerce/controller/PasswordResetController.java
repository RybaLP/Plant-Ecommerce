package com.ecommerce.controller;

import com.ecommerce.dto.auth.ForgotPasswordRequest;
import com.ecommerce.dto.auth.ResetPasswordRequest;
import com.ecommerce.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-password")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping
    public ResponseEntity<Void> sendRequestLink(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.sendRequestLink(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        passwordResetService.resetPassword(resetPasswordRequest);
        return ResponseEntity.noContent().build();
    }
}