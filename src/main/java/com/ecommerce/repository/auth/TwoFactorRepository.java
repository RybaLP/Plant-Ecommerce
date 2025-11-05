package com.ecommerce.repository.auth;

import com.ecommerce.model.auth.TwoFactor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TwoFactorRepository extends JpaRepository<TwoFactor, Long>{
    Optional<TwoFactor> findByToken (String token);
}
