package com.ecommerce.mapper;

import com.ecommerce.dto.user.ClientProfileDto;
import com.ecommerce.model.user.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CartMapper.class})
public interface ClientMapper {

    ClientProfileDto clientToClientProfileDto (Client client);
}
