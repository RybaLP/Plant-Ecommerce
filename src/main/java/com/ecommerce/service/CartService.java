package com.ecommerce.service;

import com.ecommerce.dto.cart.CartDto;
import com.ecommerce.mapper.CartMapper;
import com.ecommerce.model.cart.Cart;
import com.ecommerce.model.cart.CartItem;
import com.ecommerce.model.product.Plant;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.plant.PlantRepository;
import com.ecommerce.repository.cart.CartRepository;
import com.ecommerce.repository.user.ClientRepository;
import com.ecommerce.util.AuthenticateClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ClientRepository clientRepository;
    private final PlantRepository plantRepository;
    private final AuthenticateClient authenticateClient;

    private final CartMapper cartMapper;

    @Transactional
    public Cart createCartForClient (Client client) {

        if (client == null) {
            throw new IllegalArgumentException("Could not find client");
        }

        Cart newCart = Cart.builder()
                .client(client)
                .build();

        return cartRepository.save(newCart);
    }

    @Transactional(readOnly = true)
    public CartDto getClientCart() {

        Client client = authenticateClient.getAuthenticatedClient();

        Cart cart = client.getCart();

        if (cart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found for this client");
        }

        return cartMapper.cartToCartDto(cart);
    }


    @Transactional
    public CartDto addPlantToCart (Long plantId, int quantity) {

        Client client = authenticateClient.getAuthenticatedClient();

        Cart cart = cartRepository.findById(client.getCart().getId())
                .orElseThrow(()-> new IllegalStateException("Cart not found"));

        if (cart == null) {
            throw new IllegalStateException("Cart for client with ID " + client.getId() + " does not exist.");
        }

        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new IllegalStateException("Could not find a plant"));

        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getPlant().getId().equals(plantId))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            existingCartItem.calculateSubtotal();
        }

        else {
            CartItem cartItem = CartItem.builder()
                    .plant(plant)
                    .quantity(quantity)
                    .build();
            cartItem.calculateSubtotal();
            cart.addCartItem(cartItem);
        }

        Cart savedCart = cartRepository.save(cart);

        return cartMapper.cartToCartDto(savedCart);
    }


    @Transactional
    public void removeItemFromCart(Long plantId) {

        Client client = authenticateClient.getAuthenticatedClient();

        Cart cart = client.getCart();

        if (cart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found for this client");
        }

        cart.getCartItems().stream()
                .filter(item -> item.getPlant().getId().equals(plantId))
                .findFirst()
                .ifPresent(cart::removeCartItem);

        cartRepository.save(cart);
    }


    @Transactional
    public CartDto changeQuantity (Long plantId, Integer quantity) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(()->new IllegalStateException("Could not find client with this email"));

        Cart cart = client.getCart();

        if (cart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found for this client");
        }

        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(i -> i.getPlant().getId().equals(plantId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Product not found in cart"));


        Integer quantityInStock = cartItem.getPlant().getQuantityInStock();

        if (quantity > quantityInStock) {
            throw new IllegalStateException("There are not enough plants in store. Available: " + cartItem.getPlant().getQuantityInStock());
        }

        if (quantity < 1) {
            throw new IllegalStateException("Plant quantity can not be less than 1");
        }

        cartItem.setQuantity(quantity);

        return cartMapper.cartToCartDto(cart);
    }
}