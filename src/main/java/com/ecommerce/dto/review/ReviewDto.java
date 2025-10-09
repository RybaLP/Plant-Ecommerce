package com.ecommerce.dto.review;

import com.ecommerce.enums.Rate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {

    private Long id;
    private Rate rate;
    private String comment;
    private LocalDateTime reviewDate;
    private String username;
}
