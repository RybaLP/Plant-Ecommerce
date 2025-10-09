package com.ecommerce.model.user;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String country;

    public void update (String street, String city , String postalCode, String country){
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }
}
