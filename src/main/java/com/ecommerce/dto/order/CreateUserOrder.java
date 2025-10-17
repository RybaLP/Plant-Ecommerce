package com.ecommerce.dto.order;

import com.ecommerce.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserOrder {
    private BigDecimal deliveryPrice;
    private PaymentMethod paymentMethod;
    private boolean isCompanyOrder;
    private String nip;
    private String companyName;
}