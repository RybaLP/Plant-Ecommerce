package com.ecommerce.repository.cart;

import com.ecommerce.model.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository <Cart, Long> {}
