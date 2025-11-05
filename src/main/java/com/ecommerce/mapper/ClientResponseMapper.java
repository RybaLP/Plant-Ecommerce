package com.ecommerce.mapper;

import com.ecommerce.dto.user.ClientResponseDto;
import com.ecommerce.model.user.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface ClientResponseMapper {
    ClientResponseDto clientToClientResponseDto(Client client);
}
