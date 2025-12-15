package com.ecommerce;

import com.ecommerce.dto.Plant.PlantDto;
import com.ecommerce.dto.Plant.PlantRequestDto;
import com.ecommerce.mapper.PlantMapper;
import com.ecommerce.model.product.Plant;
import com.ecommerce.repository.plant.PlantRepository;
import com.ecommerce.service.PlantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlantServiceTest {

    private PlantRepository plantRepository;
    private PlantMapper plantMapper;
    private PlantService plantService;

    @BeforeEach
    void setUp() {
        plantRepository = mock(PlantRepository.class);
        plantMapper = mock(PlantMapper.class);
        plantService = new PlantService(plantRepository, plantMapper, null); // trzeci mapper null bo nie potrzebny tutaj
    }

    @Test
    void createPlantDto_ShouldReturnPlantDto_WhenPlantDoesNotExist() {
        PlantRequestDto requestDto = PlantRequestDto.builder()
                .name("Monstera")
                .price(BigDecimal.valueOf(50))
                .size("Medium")
                .category(null)
                .imageUrl("url")
                .quantityInStock(10)
                .description("Nice plant")
                .build();

        when(plantRepository.existsByName("Monstera")).thenReturn(false);

        Plant savedPlant = new Plant();
        savedPlant.setName("Monstera");
        savedPlant.setPrice(BigDecimal.valueOf(50));

        when(plantRepository.save(any(Plant.class))).thenReturn(savedPlant);

        PlantDto plantDto = new PlantDto();
        plantDto.setName("Monstera");

        when(plantMapper.plantToPlantDto(savedPlant)).thenReturn(plantDto);

        PlantDto result = plantService.createPlantDto(requestDto);

        assertNotNull(result);
        assertEquals("Monstera", result.getName());

        verify(plantRepository, times(1)).save(any(Plant.class));
    }

    @Test
    void createPlantDto_ShouldThrowException_WhenPlantExists() {
        PlantRequestDto requestDto = PlantRequestDto.builder()
                .name("Monstera")
                .build();


        when(plantRepository.existsByName("Monstera")).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> plantService.createPlantDto(requestDto));

        verify(plantRepository, never()).save(any(Plant.class));
    }


    @Test
    void getPlantById_ShouldThrowException_WhenPlantDoesNotExist() {
        Long plantId = 2L;

        when(plantRepository.findById(plantId)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> plantService.getPlantById(plantId));

        verify(plantRepository, times(1)).findById(plantId);
    }
}
