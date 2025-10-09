package com.ecommerce.dto.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckoutRequestDto {
    private Long orderId;
}