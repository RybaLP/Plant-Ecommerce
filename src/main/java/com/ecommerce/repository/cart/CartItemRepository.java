package com.ecommerce.repository.cart;

import com.ecommerce.model.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {}
