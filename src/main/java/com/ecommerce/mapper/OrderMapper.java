package com.ecommerce.mapper;

import com.ecommerce.dto.order.OrderDto;
import com.ecommerce.model.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, AddressMapper.class})
public interface OrderMapper {

    @Mapping(target = "deliveryPrice", source = "deliveryPrice")
    @Mapping(target = "orderNumber", source = "orderNumber")
    OrderDto orderToOrderDto(Order order);
}