package com.ecommerce.repository.auth;

import com.ecommerce.model.auth.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository <PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken (String token);
}