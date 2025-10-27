package com.ecommerce.config;

import com.ecommerce.dto.auth.AuthenticationResponseDto;
import com.ecommerce.model.cart.Cart;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.user.ClientRepository;
import com.ecommerce.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ClientRepository clientRepository;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String firstName = oauthUser.getAttribute("given_name");
        String lastName = oauthUser.getAttribute("family_name");

        Optional<Client> optionalClient = clientRepository.findByEmail(email);
        Client client;

        if (optionalClient.isPresent()) {
            client = optionalClient.get();
        } else {
            client = Client.builder()
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .registrationDate(LocalDateTime.now())
                    .role(com.ecommerce.enums.UserRole.CLIENT)
                    .build();

            Cart cart = Cart.builder()
                    .client(client)
                    .totalPrice(BigDecimal.ZERO)
                    .build();
            client.setCart(cart);

            clientRepository.save(client);
        }

        String token = jwtService.generateToken(client);
        String refreshToken = jwtService.generateRefreshToken(client);

        String redirectUrl = "http://localhost:5173/oauth2/redirect?token=" + token;
        response.sendRedirect(redirectUrl);
    }
}
