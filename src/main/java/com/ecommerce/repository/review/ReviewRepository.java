package com.ecommerce.repository.review;

import com.ecommerce.model.product.Plant;
import com.ecommerce.model.product.Review;
import com.ecommerce.model.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByClientAndPlant (Client client , Plant plant);

}