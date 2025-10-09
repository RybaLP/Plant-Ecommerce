package com.ecommerce.model.product;

import com.ecommerce.enums.Rate;
import com.ecommerce.model.user.Client;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"client_id", "plant_id"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Rate rate;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private LocalDateTime reviewDate;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;
}
