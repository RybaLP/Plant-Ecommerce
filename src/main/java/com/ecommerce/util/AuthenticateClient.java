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

        public Client getAuthenticatedClient () {

            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            Client authenticatedClient = clientRepository.findByEmail(email)
                    .orElseThrow(()->new IllegalStateException("Couldn't authenticate an user"));

            return authenticatedClient;
        }
}
