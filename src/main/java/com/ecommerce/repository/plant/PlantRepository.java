package com.ecommerce.repository.plant;

import com.ecommerce.enums.PlantType;
import com.ecommerce.model.product.Plant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public interface PlantRepository extends JpaRepository <Plant, Long> {
    boolean existsByName(String name);
    Page<Plant> findByCategory(PlantType category, Pageable pageable);
    Page<Plant> findByCategoryIn(Collection<PlantType> categories, Pageable pageable);
}