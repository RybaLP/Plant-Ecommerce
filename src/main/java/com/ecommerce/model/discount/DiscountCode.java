package com.ecommerce.model.discount;

import com.ecommerce.model.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="discountCodes")
public class DiscountCode {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private Double discountPercentage;

    private LocalDate expirationDate;

    @Column(nullable = false)
    private Boolean used;

    @OneToMany(mappedBy = "discountCode")
    private Set<Order> orders;
}
