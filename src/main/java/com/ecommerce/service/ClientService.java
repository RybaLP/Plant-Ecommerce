package com.ecommerce.service;

import com.ecommerce.dto.user.ClientContactInfoDto;
import com.ecommerce.dto.user.ClientProfileDto;
import com.ecommerce.mapper.AddressMapper;
import com.ecommerce.mapper.ClientMapper;
import com.ecommerce.model.user.Address;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.user.ClientRepository;
import com.ecommerce.util.AuthenticateClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientMapper clientMapper;
    private final AuthenticateClient authenticateClient;
    private final ClientRepository clientRepository;
    private final AddressMapper addressMapper;

    public ClientProfileDto getClientInfo () {
        Client client = authenticateClient.getAuthenticatedClient();
        return clientMapper.clientToClientProfileDto(client);
    }

    public ClientContactInfoDto getClientContactInfoDto () {
        Client client = authenticateClient.getAuthenticatedClient();
        Address clientAddress = client.getShippingAddress();
        String clientPhoneNumber = client.getPhoneNumber();

        if (clientAddress == null) {
            throw new IllegalStateException("Client does not have provided Address!");
        }

        return ClientContactInfoDto.builder()
                .address(addressMapper.addressToAddressDto(clientAddress))
                .phoneNumber(clientPhoneNumber)
                .build();
    }
}