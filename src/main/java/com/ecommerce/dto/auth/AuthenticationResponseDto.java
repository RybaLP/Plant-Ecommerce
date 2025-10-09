package com.ecommerce.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponseDto {
    private String token;
    private String refreshToken;
}
