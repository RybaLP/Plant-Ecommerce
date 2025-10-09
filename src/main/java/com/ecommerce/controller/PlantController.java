package com.ecommerce.controller;

import com.ecommerce.dto.Plant.PlantDetailDto;
import com.ecommerce.dto.Plant.PlantDto;
import com.ecommerce.mapper.PlantMapper;
import com.ecommerce.model.product.Plant;
import com.ecommerce.repository.plant.PlantRepository;
import com.ecommerce.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/plant")
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;

    @GetMapping("/landing-page")
    public ResponseEntity<List<PlantDto>> getLandingPagePlants (){
        List<PlantDto> plantDtos = plantService.getLandingPagePLants();
        System.out.println(">>> DTO: " + plantDtos);
        return ResponseEntity.ok(plantDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantDetailDto> getPLantById (@PathVariable("id") Long plantId) {
        PlantDetailDto plantDetailDto = plantService.getPlantById(plantId);
        return ResponseEntity.ok(plantDetailDto);
    }
}
