package com.ecommerce.model.order;


import com.ecommerce.enums.OrderStatus;
import com.ecommerce.model.cart.CartItem;
import com.ecommerce.model.discount.DiscountCode;
import com.ecommerce.model.user.Address;
import com.ecommerce.model.user.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id", nullable = false)
    private Address shippingAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "clientId", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "discountCodeId")
    private DiscountCode discountCode;

    public void setOrderDateToNow () {
        this.orderDate = LocalDateTime.now();
    }

    public void recalculateTotalPrice(){

    BigDecimal itemsPrice = this.orderItems.stream()
        .map(OrderItem::getPriceAtPurchase)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (this.discountCode != null && this.discountCode.getDiscountPercentage() > 0){
            double discountFactor = 1 - this.discountCode.getDiscountPercentage();
            this.totalPrice = itemsPrice.multiply(BigDecimal.valueOf(discountFactor));
        }
        else {
            this.totalPrice = itemsPrice;
        }
    }
}