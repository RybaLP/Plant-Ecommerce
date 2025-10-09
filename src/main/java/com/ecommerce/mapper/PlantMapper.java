package com.ecommerce.mapper;

import com.ecommerce.dto.Plant.PlantDto;
import com.ecommerce.model.product.Plant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlantMapper {
    PlantDto plantToPlantDto(Plant plant);
}
