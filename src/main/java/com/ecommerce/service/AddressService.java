package com.ecommerce.service;

import com.ecommerce.dto.user.AddressDto;
import com.ecommerce.mapper.AddressMapper;
import com.ecommerce.model.user.Address;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.user.AddressRepository;
import com.ecommerce.util.AuthenticateClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AddressService {

    private final AuthenticateClient authenticateClient;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Transactional
    public AddressDto updateAddress(AddressDto addressDto) {

        Client client = authenticateClient.getAuthenticatedClient();

        Address address = client.getShippingAddress();

        if (address == null) {
            throw new IllegalStateException("Client does not have a shipping address");
        }

        if (addressDto.getCity() != null) {
            address.setCity(addressDto.getCity());
        }

        if (addressDto.getCountry() != null) {
            address.setCountry(addressDto.getCountry());
        }

        if (addressDto.getPostalCode() != null) {
            address.setPostalCode(addressDto.getPostalCode());
        }

        if (addressDto.getStreet() != null) {
            address.setStreet(addressDto.getStreet());
        }

        addressRepository.save(address);
        return addressMapper.addressToAddressDto(address);
    }
}
