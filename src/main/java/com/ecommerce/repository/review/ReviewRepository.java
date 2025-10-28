package com.ecommerce.repository.review;

import com.ecommerce.model.product.Plant;
import com.ecommerce.model.product.Review;
import com.ecommerce.model.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByClientAndPlant (Client client , Plant plant);

    @Query("SELECT r FROM Review r WHERE r.plant.id = :id")
    List<Review> getReviewsByPlantId(@Param("id") Long id);
}