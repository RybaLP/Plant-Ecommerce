package com.ecommerce.dto.order;

import com.ecommerce.dto.Plant.PlantDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {
    private Long id;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
    private PlantDto plant;
}
