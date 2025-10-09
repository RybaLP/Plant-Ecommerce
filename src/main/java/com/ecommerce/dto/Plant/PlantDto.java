package com.ecommerce.dto.Plant;

import com.ecommerce.enums.PlantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlantDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private PlantType category;
}
