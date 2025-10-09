package com.ecommerce.dto.review;

import com.ecommerce.enums.Rate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditReviewDto {
    private Rate rate ;
    private String comment;
}
