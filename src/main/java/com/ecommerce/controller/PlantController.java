package com.ecommerce.controller;

import com.ecommerce.dto.Plant.PlantDetailDto;
import com.ecommerce.dto.Plant.PlantDto;
import com.ecommerce.dto.Plant.PlantRequestDto;
import com.ecommerce.enums.PlantType;
import com.ecommerce.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/admin")
    public ResponseEntity<PlantDto> createPlant (@RequestBody PlantRequestDto plantRequestDto) {
        PlantDto response = plantService.createPlantDto(plantRequestDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<PlantDto> updatePlant (@RequestBody PlantRequestDto plantRequestDto, @PathVariable(value = "id") Long id) {
        PlantDto response = plantService.updatePlant(plantRequestDto, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deletePlant (@PathVariable(name = "id") Long id) {
        plantService.deletePlantById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ornamental")
    public ResponseEntity<Page<PlantDto>> getOrnamentalPlants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        List<PlantType> ornamentalCategories = List.of(
                PlantType.HERBAL,
                PlantType.FLOWERING,
                PlantType.OTHER
        );

        Page<PlantDto> result = plantService.getOrnamentalPlants(pageable, ornamentalCategories);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/category/{category}")
    public ResponseEntity<Page<PlantDto>> getPlantsByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @PathVariable("category") PlantType plantType
    ) {
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<PlantDto> result = plantService.getPlantsByCategory(pageRequest, plantType);
        return ResponseEntity.ok(result);
    }
}
