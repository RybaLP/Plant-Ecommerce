package com.ecommerce.service;

import com.ecommerce.dto.user.ClientContactInfoDto;
import com.ecommerce.dto.user.ClientProfileDto;
import com.ecommerce.mapper.AddressMapper;
import com.ecommerce.mapper.ClientMapper;
import com.ecommerce.model.user.Address;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.user.AddressRepository;
import com.ecommerce.repository.user.ClientRepository;
import com.ecommerce.util.AuthenticateClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientMapper clientMapper;
    private final AuthenticateClient authenticateClient;
    private final ClientRepository clientRepository;
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;

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


    @Transactional
    public ClientContactInfoDto createClientContactInfo (ClientContactInfoDto clientContactInfoDto) {

        Address address = Address.builder()
                .city(clientContactInfoDto.getAddress().getCity())
                .country(clientContactInfoDto.getAddress().getCountry())
                .postalCode(clientContactInfoDto.getAddress().getPostalCode())
                .street(clientContactInfoDto.getAddress().getStreet())
                .build();

        addressRepository.save(address);

        Client client = authenticateClient.getAuthenticatedClient();

        client.setPhoneNumber(clientContactInfoDto.getPhoneNumber());
        client.setShippingAddress(address);

        clientRepository.save(client);

        return ClientContactInfoDto.builder()
                .phoneNumber(clientContactInfoDto.getPhoneNumber())
                .address(clientContactInfoDto.getAddress())
                .build();
    }
}