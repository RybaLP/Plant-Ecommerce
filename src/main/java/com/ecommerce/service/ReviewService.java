package com.ecommerce.service;

import com.ecommerce.dto.Plant.PlantDto;
import com.ecommerce.dto.review.ReviewDto;
import com.ecommerce.enums.Rate;
import com.ecommerce.mapper.PlantMapper;
import com.ecommerce.mapper.ReviewMapper;
import com.ecommerce.model.order.Order;
import com.ecommerce.model.order.OrderItem;
import com.ecommerce.model.product.Plant;
import com.ecommerce.model.product.Review;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.order.OrderItemRepository;
import com.ecommerce.repository.order.OrderRepository;
import com.ecommerce.repository.plant.PlantRepository;
import com.ecommerce.repository.review.ReviewRepository;
import com.ecommerce.repository.user.ClientRepository;
import com.ecommerce.util.AuthenticateClient;
import com.stripe.model.Plan;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClientRepository clientRepository;
    private final PlantRepository plantRepository;
    private final OrderRepository orderRepository;
    private final ReviewMapper reviewMapper;
    private final AuthenticateClient authenticateClient;
    private final PlantMapper plantMapper;
    private final OrderItemRepository orderItemRepository;

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

        List<OrderItem> items = orderItemRepository.findAllByClientAndPlantId(clientId, plantId);
        items.forEach(item -> item.setReviewed(true));
        orderItemRepository.saveAll(items);

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


    public List<ReviewDto> findUserReviews() {
        Client client = authenticateClient.getAuthenticatedClient();
        if (client == null) {
            throw new IllegalStateException("User not authenticated");
        }

        List<Review> reviews = client.getReviews();
        if (reviews == null) {
            reviews = Collections.emptyList();
        }

        return reviews.stream()
                .map(reviewMapper::reviewToReviewDto)
                .toList();
    }


    public List<PlantDto> getUnreviewedPlantsFromClient () {

        Client client = authenticateClient.getAuthenticatedClient();
        if (client == null) {
            throw new IllegalStateException("Could not find client");
        }

        List<Order> orders = orderRepository.findByClientId(client.getId());

        List<OrderItem> unreviewedItems = orders.stream().flatMap(order -> order.getOrderItems().stream())
                .filter(orderItem -> !orderItem.isReviewed())
                .toList();

        Set<Plant> uniquePlants = unreviewedItems.stream()
                .map(OrderItem :: getPlant)
                .collect(Collectors.toSet());

        return uniquePlants.stream()
                .filter(plant -> !reviewRepository.existsByClientAndPlant(client, plant))
                .map(plantMapper :: plantToPlantDto)
                .toList();
    }


    public List<ReviewDto> getPlantReviews (Long id) {
        List<Review> reviews = reviewRepository.getReviewsByPlantId(id);
        return reviews.stream().map(reviewMapper :: reviewToReviewDto)
                .toList();
    }
}