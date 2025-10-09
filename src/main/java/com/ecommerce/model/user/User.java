package com.ecommerce.model.user;

import com.ecommerce.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
//@Inheritance(strategy = InheritanceType.JOINED)
@MappedSuperclass
public abstract class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    private String firstName;

    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipping_address_id", nullable = false)
    private Address shippingAddress;

    private String phoneNumber;

    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    private UserRole role;

//    jwt
    @Override
    public Collection < ? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

//    this is for user details
    @Override
    public String getUsername() {
        return this.email;
    }
}