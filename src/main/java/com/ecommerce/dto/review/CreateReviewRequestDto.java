package com.ecommerce.dto.review;

import com.ecommerce.enums.Rate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequestDto {

    private Rate rate;

    private String comment;

    private Long plantId;
}
