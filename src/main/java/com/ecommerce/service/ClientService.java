package com.ecommerce.service;

import com.ecommerce.dto.user.ClientProfileDto;
import com.ecommerce.dto.user.RegisterDto;
import com.ecommerce.enums.UserRole;
import com.ecommerce.mapper.ClientMapper;
import com.ecommerce.model.user.Address;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.user.AddressRepository;
import com.ecommerce.repository.user.ClientRepository;
import com.ecommerce.util.AuthenticateClient;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientMapper clientMapper;
    private final AuthenticateClient authenticateClient;

    public ClientProfileDto getClientInfo () {
        Client client = authenticateClient.getAuthenticatedClient();
        return clientMapper.clientToClientProfileDto(client);
    }
}