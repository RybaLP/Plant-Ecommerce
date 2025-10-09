package com.ecommerce.mapper;

import com.ecommerce.dto.user.AddressDto;
import com.ecommerce.model.user.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto addressToAddressDto (Address address);
}
