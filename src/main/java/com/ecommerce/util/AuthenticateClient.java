package com.ecommerce.util;

import com.ecommerce.model.user.Client;
import com.ecommerce.repository.user.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticateClient {

        private final ClientRepository clientRepository;

//        public Client getAuthenticatedClient () {
//
//            String email = SecurityContextHolder.getContext().getAuthentication().getName();
//
//            Client authenticatedClient = clientRepository.findByEmail(email)
//                    .orElseThrow(()->new IllegalStateException("Couldn't authenticate an user"));
//
//            return authenticatedClient;
//        }

    public Client getAuthenticatedClient() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = null;

        if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            email = userDetails.getUsername();
        } else if (auth.getPrincipal() instanceof org.springframework.security.oauth2.core.user.OAuth2User oAuth2User) {
            email = oAuth2User.getAttribute("email");
        }

        if (email == null) {
            throw new IllegalStateException("Couldn't get email from authentication");
        }

        System.out.println(email);

        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Couldn't authenticate an user"));
    }
}
