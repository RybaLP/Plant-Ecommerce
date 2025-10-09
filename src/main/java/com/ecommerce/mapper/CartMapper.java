package com.ecommerce.mapper;

import com.ecommerce.dto.cart.CartDto;
import com.ecommerce.model.cart.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {
    CartDto cartToCartDto(Cart cart);
}