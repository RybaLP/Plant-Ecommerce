package com.ecommerce.model.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.ecommerce.model.product.Plant;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name="cartItems")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @ManyToOne
    @JoinColumn(name = "cartId", nullable = false)
    @JsonIgnore
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "plantId", unique = false)
    private Plant plant;

    public void calculateSubtotal(){
        if(this.plant != null && this.plant.getPrice() != null && this.quantity != null){
            this.subtotal = this.plant.getPrice().multiply(new BigDecimal(this.quantity));
        }
        else {
            this.subtotal = BigDecimal.ZERO;
        }
    }
}
