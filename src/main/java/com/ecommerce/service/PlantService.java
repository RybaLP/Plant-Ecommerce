package com.ecommerce.service;

import com.ecommerce.dto.Plant.PlantDetailDto;
import com.ecommerce.dto.Plant.PlantDto;
import com.ecommerce.dto.Plant.PlantRequestDto;
import com.ecommerce.enums.PlantType;
import com.ecommerce.mapper.PlantDetailMapper;
import com.ecommerce.mapper.PlantMapper;
import com.ecommerce.model.product.Plant;
import com.ecommerce.repository.plant.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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


    @Transactional(readOnly = true)
//    @Cacheable(value = "plants", key = "'allPlants'")
    public List<PlantDto> getAllPlants () {
        return plantRepository.findAll()
                .stream()
                .map(plantMapper :: plantToPlantDto)
                .toList();
    }


    @Transactional
//    @CacheEvict(value = "plants", allEntries = true)
    public PlantDto createPlantDto (PlantRequestDto plantRequestDto) {
        boolean doesExist = plantRepository.existsByName(plantRequestDto.getName());
        if (doesExist) {
            throw new IllegalStateException("Plant with this name already exists in database!");
        }

        Plant newPlant = Plant.builder()
                .name(plantRequestDto.getName())
                .price(plantRequestDto.getPrice())
                .size(plantRequestDto.getSize())
                .category(plantRequestDto.getCategory())
                .imageUrl(plantRequestDto.getImageUrl())
                .quantityInStock(plantRequestDto.getQuantityInStock())
                .description(plantRequestDto.getDescription())
                .build();

        Plant plant = plantRepository.save(newPlant);

        return plantMapper.plantToPlantDto(plant);
    }



    @Transactional
    @CacheEvict(value = "plants", allEntries = true)
    public PlantDto updatePlant (PlantRequestDto plantRequestDto, Long id) {

        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User with this id does not exist in data base"));

        if (plantRequestDto.getName() != null && !plantRequestDto.getName().isBlank()) {
            plant.setName(plantRequestDto.getName());
        }

        if (plantRequestDto.getPrice() != null) {
            plant.setPrice(plantRequestDto.getPrice());
        }

        if (plantRequestDto.getImageUrl() != null && !plantRequestDto.getImageUrl().isBlank()) {
            plant.setImageUrl(plantRequestDto.getImageUrl());
        }

        if (plantRequestDto.getCategory() != null) {
            plant.setCategory(plantRequestDto.getCategory());
        }

        if (plantRequestDto.getSize() != null && !plantRequestDto.getSize().isBlank()) {
            plant.setSize(plantRequestDto.getSize());
        }

        if (plantRequestDto.getQuantityInStock() != null) {
            plant.setQuantityInStock(plantRequestDto.getQuantityInStock());
        }

        plantRepository.save(plant);

        return plantMapper.plantToPlantDto(plant);
    }


    @Transactional
    public void deletePlantById (Long id) {
        plantRepository.deleteById(id);
    }

    public Page<PlantDto> getPlantsByCategory(Pageable pageable, PlantType plantType) {
        return plantRepository.findByCategory(plantType, pageable)
                .map(plantMapper :: plantToPlantDto);
    }

    public Page<PlantDto> getOrnamentalPlants (Pageable pageable, List<PlantType> categories) {
        return plantRepository.findByCategoryIn(
                categories,
                pageable
        ).map(plantMapper :: plantToPlantDto);
    }
}