package com.ecommerce.dto.order;

import com.ecommerce.enums.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderStatusDto {

    @NotNull
    private OrderStatus orderStatus;
}
