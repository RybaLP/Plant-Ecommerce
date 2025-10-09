package com.ecommerce.mapper;

import com.ecommerce.dto.Plant.PlantDetailDto;
import com.ecommerce.model.product.Plant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ReviewMapper.class)
public interface PlantDetailMapper {

    PlantDetailDto plantToPlantDetailDto (Plant plant);
}
