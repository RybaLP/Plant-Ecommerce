package com.ecommerce.mapper;

import com.ecommerce.dto.cart.CartItemDto;
import com.ecommerce.model.cart.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PlantMapper.class})
public interface CartItemMapper {
    CartItemDto cartItemToCartItemDto (CartItem cartItem);
}
