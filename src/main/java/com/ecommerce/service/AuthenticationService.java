package com.ecommerce.service;

import com.ecommerce.dto.auth.AuthenticationResponseDto;
import com.ecommerce.dto.auth.LoginRequestDto;
import com.ecommerce.dto.auth.RegisterRequestDto;
import com.ecommerce.enums.UserRole;
import com.ecommerce.model.cart.Cart;
import com.ecommerce.model.user.Address;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.user.AddressRepository;
import com.ecommerce.repository.user.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    @Transactional
    public AuthenticationResponseDto registerUser (RegisterRequestDto registerRequestDto){

        Address shippingAddress = Address.builder()
                .street(registerRequestDto.getShippingAddress().getStreet())
                .city(registerRequestDto.getShippingAddress().getCity())
                .country(registerRequestDto.getShippingAddress().getCountry())
                .postalCode(registerRequestDto.getShippingAddress().getPostalCode())
                .build();

        Address savedAddress = addressRepository.save(shippingAddress);

        Client client = Client.builder()
                .username(registerRequestDto.getUsername())
                .firstName(registerRequestDto.getFirstName())
                .lastName(registerRequestDto.getLastName())
                .email(registerRequestDto.getEmail())
                .phoneNumber(registerRequestDto.getPhoneNumber())
                .registrationDate(LocalDateTime.now())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .role(UserRole.CLIENT)
                .shippingAddress(savedAddress)
                .build();

        Cart cart = Cart.builder()
                        .client(client)
                        .totalPrice(BigDecimal.ZERO)
                        .build();

        client.setCart(cart);
        clientRepository.save(client);

        var jwtToken = jwtService.generateToken(client);
        var refreshToken = jwtService.generateRefreshToken(client);

        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    public AuthenticationResponseDto authenticate (LoginRequestDto loginRequestDto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        Client client = clientRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new IllegalStateException(""));

        String token = jwtService.generateToken(client);
        String refreshToken = jwtService.generateRefreshToken(client);

        return AuthenticationResponseDto.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
}
