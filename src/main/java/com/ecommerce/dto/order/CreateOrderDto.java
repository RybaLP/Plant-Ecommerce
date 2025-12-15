package com.ecommerce.dto.order;

import com.ecommerce.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class CreateOrderDto {
    Long orderId;
    String orderNumber;
    BigDecimal totalPrice;
    OrderStatus status;
}