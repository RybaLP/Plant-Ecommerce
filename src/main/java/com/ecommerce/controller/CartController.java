package com.ecommerce.controller;

import com.ecommerce.dto.cart.AddToCartRequestDto;
import com.ecommerce.dto.cart.CartDto;
import com.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/cart")
@RequiredArgsConstructor
@RestController
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDto> getCart() {
        return ResponseEntity.ok(cartService.getClientCart());
    }

    @PostMapping
    public ResponseEntity<CartDto> addCartItem (@Valid @RequestBody AddToCartRequestDto addToCartRequestDto) {
        CartDto cartDto = cartService.addPlantToCart(addToCartRequestDto.getPlantId(), addToCartRequestDto.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
    }

    @DeleteMapping("/item/{plantId}")
    public ResponseEntity<Void> deleteCartItem (@PathVariable Long plantId) {
        cartService.removeItemFromCart(plantId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/item/{plantId}/{quantity}")
    public ResponseEntity<CartDto> changeQuantity (@PathVariable Long plantId, @PathVariable Integer quantity) {
        CartDto cartDto = cartService.changeQuantity(plantId, quantity);
        return ResponseEntity.ok(cartDto);
    }
}
