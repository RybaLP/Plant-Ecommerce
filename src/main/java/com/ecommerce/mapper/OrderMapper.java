package com.ecommerce.mapper;

import com.ecommerce.dto.order.OrderDto;
import com.ecommerce.model.order.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, AddressMapper.class})
public interface OrderMapper {
    OrderDto orderToOrderDto(Order order);
}