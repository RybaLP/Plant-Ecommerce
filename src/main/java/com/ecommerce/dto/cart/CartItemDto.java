package com.ecommerce.dto.cart;

import com.ecommerce.dto.Plant.PlantDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartItemDto {
    private Long id;
    private Integer quantity;
    private BigDecimal subtotal;
    private PlantDto plant;
}
