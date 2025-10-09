package com.ecommerce.model.user;


import com.ecommerce.model.cart.Cart;
import com.ecommerce.model.order.Order;
import com.ecommerce.model.product.Review;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "clients")
public class Client extends User {

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "client")
    private Set<Order> orders;

    @OneToMany(mappedBy = "client" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

}