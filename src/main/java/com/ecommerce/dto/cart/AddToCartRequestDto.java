package com.ecommerce.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddToCartRequestDto {

    @NotNull(message = "PlantId cannot be null")
    @Min(value = 1, message = "Plant ID must be greater or equal to 1")
    private Long plantId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
