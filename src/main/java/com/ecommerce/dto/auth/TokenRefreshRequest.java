package com.ecommerce.dto.auth;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}