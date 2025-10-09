package com.ecommerce.model.cart;

import com.ecommerce.model.user.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalPrice = BigDecimal.ZERO;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "clientId", unique = true)
    private Client client;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    public void addCartItem(CartItem cartItem) {
        cartItem.setCart(this);
        this.cartItems.add(cartItem);
        this.recalculateTotalPrice();
    }

    public void removeCartItem(CartItem cartItem){
        this.cartItems.remove(cartItem);
        this.recalculateTotalPrice();
    }

    public void recalculateTotalPrice(){
        this.totalPrice = this.cartItems.stream()
                .map(item -> {
                    item.calculateSubtotal();
                    return item.getSubtotal();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
