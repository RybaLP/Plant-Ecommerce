package com.ecommerce.model.order;


import com.ecommerce.enums.OrderStatus;
import com.ecommerce.enums.PaymentMethod;
import com.ecommerce.model.discount.DiscountCode;
import com.ecommerce.model.user.Address;
import com.ecommerce.model.user.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


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
    @JoinColumn(name = "shipping_address_id", nullable = true)
    private Address shippingAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private BigDecimal deliveryPrice;


    @Column(unique = true, nullable = false)
    private String orderNumber;

    @Column(nullable = false)
    private boolean isCompanyOrder;

    private String nip;
    private String companyName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "clientId", nullable = true)
    private Client client;

//  guest props
    private String guestFirstName;
    private String guestLastName;
    private String guestEmail;
    private String guestPhone;
//

    private String street;
    private String city;
    private String postalCode;
    private String country;

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

    public String generateOrderNumber () {
        return UUID.randomUUID().toString().substring(0,8).toUpperCase();
    }
}