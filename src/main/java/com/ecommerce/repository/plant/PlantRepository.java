package com.ecommerce.repository.plant;

import com.ecommerce.model.product.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlantRepository extends JpaRepository <Plant, Long> {

}