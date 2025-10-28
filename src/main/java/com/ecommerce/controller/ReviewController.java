package com.ecommerce.controller;

import com.ecommerce.dto.Plant.PlantDto;
import com.ecommerce.dto.review.CreateReviewRequestDto;
import com.ecommerce.dto.review.EditReviewDto;
import com.ecommerce.dto.review.ReviewDto;
import com.ecommerce.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody CreateReviewRequestDto dto) {
        ReviewDto review = reviewService.addReview(dto.getRate(), dto.getComment(), dto.getPlantId());
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReviewDto> editReview(@PathVariable Long id, @Valid @RequestBody EditReviewDto dto) {
        ReviewDto review = reviewService.editReview(dto.getRate(), dto.getComment(), id);
        return ResponseEntity.ok(review);
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.findAllReviews());
    }


    @GetMapping("/user")
    public ResponseEntity<List<ReviewDto>> getUserReviews () {
        return ResponseEntity.ok(reviewService.findUserReviews());
    }

    @GetMapping("/user/not-reviewed")
    public ResponseEntity<List<PlantDto>> getNotReviewedItems () {
        return ResponseEntity.ok(reviewService.getUnreviewedPlantsFromClient());
    }

    @GetMapping("/plant/{id}")
    public ResponseEntity<List<ReviewDto>> getReviewsByPlantId(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getPlantReviews(id));
    }

}