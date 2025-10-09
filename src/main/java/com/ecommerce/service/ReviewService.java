package com.ecommerce.service;

import com.ecommerce.dto.review.ReviewDto;
import com.ecommerce.enums.Rate;
import com.ecommerce.mapper.ReviewMapper;
import com.ecommerce.model.product.Plant;
import com.ecommerce.model.product.Review;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.order.OrderRepository;
import com.ecommerce.repository.plant.PlantRepository;
import com.ecommerce.repository.review.ReviewRepository;
import com.ecommerce.repository.user.ClientRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClientRepository clientRepository;
    private final PlantRepository plantRepository;
    private final OrderRepository orderRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewDto addReview (Rate rate, String comment, Long plantId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Client client = clientRepository.findByEmail(email)
                .orElseThrow(()->new IllegalStateException("Client not found"));

        Long clientId = client.getId();

        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(()->new IllegalStateException("Plant with id" + plantId + "not found"));


        if (reviewRepository.existsByClientAndPlant(client,plant)){
            throw new IllegalArgumentException("User already reviewed this product");
        }

        if (!(orderRepository.existsByClientIdAndOrderItemsPlantId(clientId, plantId))) {
            throw new IllegalStateException("User didnt buy this product");
        }

        Review review = Review.builder()
                .client(client)
                .plant(plant)
                .rate(rate)
                .comment(comment)
                .reviewDate(LocalDateTime.now())
                .build();

        Review saved = reviewRepository.save(review);

        return reviewMapper.reviewToReviewDto(saved);
    }


    @Transactional
    public void deleteReview (Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new IllegalStateException("Review not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        if (!review.getClient().getEmail().equals(email)) {
            throw new SecurityException("You can delete only your own reviews");
        }

        reviewRepository.delete(review);
    }

    @Transactional
    public ReviewDto editReview (Rate rate, String comment, Long reviewId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email  = authentication.getName();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new IllegalStateException("Review was not found "));

        if (!review.getClient().getEmail().equals(email)) {
            throw new IllegalArgumentException("Access denied. The review belongs to a different user");
        }

        if(rate != null && !review.getRate().equals(rate)) {
            review.setRate(rate);
        }

        if (comment != null && !comment.equals(review.getComment())) {
            review.setComment(comment);
        }

        review.setReviewDate(LocalDateTime.now());

        Review editedReview = reviewRepository.save(review);

        return reviewMapper.reviewToReviewDto(editedReview);
    }

    public List<ReviewDto> findAllReviews () {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper :: reviewToReviewDto)
                .toList();
    }

}