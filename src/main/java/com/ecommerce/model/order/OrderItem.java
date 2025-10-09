package com.ecommerce.model.order;

import com.ecommerce.model.product.Plant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orderItems")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal priceAtPurchase;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "plantId", nullable = false)
    private Plant plant;

    public void setPriceFromPlant(Plant plant) {
        if(plant != null && plant.getPrice() != null ){
            this.priceAtPurchase = plant.getPrice();
        } else {
            throw new IllegalArgumentException("Plant or plant price cannot be nullable");
        }
    }

    public BigDecimal calculateSubtotal () {
        if(this.priceAtPurchase != null && this.quantity != null) {
            return this.priceAtPurchase.multiply(BigDecimal.valueOf(this.quantity));
        }
//        when error return Zero
        return BigDecimal.ZERO;
    }
}