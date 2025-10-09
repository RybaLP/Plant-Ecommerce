package com.ecommerce.mapper;

import com.ecommerce.dto.review.ReviewDto;
import com.ecommerce.model.product.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "username", source = "client.username")
    ReviewDto reviewToReviewDto(Review review);
}
