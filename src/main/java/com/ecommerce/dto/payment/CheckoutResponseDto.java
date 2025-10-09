package com.ecommerce.dto.payment;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CheckoutResponseDto {
    private String stripeCheckoutUrl;
}