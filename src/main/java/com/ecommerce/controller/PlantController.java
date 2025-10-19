package com.ecommerce.controller;

import com.ecommerce.dto.Plant.PlantDetailDto;
import com.ecommerce.dto.Plant.PlantDto;
import com.ecommerce.service.PlantService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public ResponseEntity<List<PlantDto>> getAllPlants () {
        return ResponseEntity.ok(plantService.getAllPlants());
    }
}
