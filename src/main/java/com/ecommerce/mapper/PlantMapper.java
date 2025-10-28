package com.ecommerce.mapper;

import com.ecommerce.dto.Plant.PlantDto;
import com.ecommerce.model.product.Plant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlantMapper {

    @Mapping(source = "size", target = "size")
    @Mapping(source = "quantityInStock", target = "quantityInStock")
    PlantDto plantToPlantDto(Plant plant);
}
