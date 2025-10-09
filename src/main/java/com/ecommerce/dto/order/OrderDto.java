package com.ecommerce.dto.order;

import com.ecommerce.dto.user.AddressDto;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.model.user.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderDto {
    private Long id;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private AddressDto shippingAddress;
    private OrderStatus status;
    private List<OrderItemDto> orderItems;
}