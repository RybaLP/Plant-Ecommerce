package com.ecommerce.dto.Plant;

import com.ecommerce.dto.review.ReviewDto;
import com.ecommerce.enums.PlantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlantDetailDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private PlantType category;
    private String description;
    private Integer quantityInStock;
    private List<ReviewDto> reviews;
}