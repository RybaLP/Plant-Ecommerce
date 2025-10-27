package com.ecommerce.model.product;

import com.ecommerce.enums.PlantType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantityInStock;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private PlantType category;

    @Column(columnDefinition = "TEXT")
    private String size;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public boolean isAvalible(){
        return this.quantityInStock > 0;
    }

    public void decreateQuantity(int amount){

        if(this.isAvalible() && this.quantityInStock >= amount) {
            this.quantityInStock -= amount;
        } else {
            throw new IllegalArgumentException("Could not decrease amount of plants");
        }
    }
}
