package com.ecommerce.model.auth;

import com.ecommerce.model.user.Client;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private Client user;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    public boolean isExpired() {
        return expirationDate.isBefore(LocalDateTime.now());
    }
}