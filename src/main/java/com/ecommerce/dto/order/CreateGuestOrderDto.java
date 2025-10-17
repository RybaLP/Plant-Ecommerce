package com.ecommerce.dto.order;

import com.ecommerce.dto.cart.CartItemDto;
import com.ecommerce.dto.user.AddressDto;
import com.ecommerce.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGuestOrderDto {
    private String guestFirstName;
    private String guestLastName;
    private String guestEmail;
    private String guestPhone;
    private AddressDto shippingAddress;
    private BigDecimal deliveryPrice;
    private List<CartItemDto> cartItems;
    private PaymentMethod paymentMethod;
    private boolean isCompanyOrder;

//    optiona fields
    private String nip;
    private String companyName;
}