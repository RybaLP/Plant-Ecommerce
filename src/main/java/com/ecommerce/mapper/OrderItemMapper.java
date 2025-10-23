package com.ecommerce.mapper;

import com.ecommerce.dto.order.OrderItemDto;
import com.ecommerce.model.order.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PlantMapper.class})
public interface OrderItemMapper {
    OrderItemDto orderItemToOrderItemDto(OrderItem orderItem);
}
