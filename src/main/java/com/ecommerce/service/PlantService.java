package com.ecommerce.service;

import com.ecommerce.dto.Plant.PlantDetailDto;
import com.ecommerce.dto.Plant.PlantDto;
import com.ecommerce.mapper.PlantDetailMapper;
import com.ecommerce.mapper.PlantMapper;
import com.ecommerce.model.product.Plant;
import com.ecommerce.repository.plant.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository plantRepository;
    private final PlantMapper plantMapper;
    private final PlantDetailMapper plantDetailMapper;

//    get first 10 offers for landing page
    @Transactional(readOnly = true)
    public List<PlantDto> getLandingPagePLants () {

        Pageable pageable = PageRequest.of(0,10, Sort.by("id").descending());

        Page<Plant> plants = plantRepository.findAll(pageable);

        return plants.getContent()
                .stream()
                .map(plantMapper :: plantToPlantDto)
                .toList();
    }


    @Transactional(readOnly = true)
    public PlantDetailDto getPlantById (Long plantId) {

        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(()->new IllegalStateException("Could not find plant with id" + plantId));

        return plantDetailMapper.plantToPlantDetailDto(plant);
    }
}