package com.ecommerce.dto.Plant;

import com.ecommerce.enums.PlantType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PlantRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantityInStock;
    private String imageUrl;
    private PlantType category;
    private String size;
}
