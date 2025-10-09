package com.ecommerce.service;

import com.ecommerce.dto.order.OrderDto;
import com.ecommerce.dto.order.OrderItemDto;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.mapper.OrderMapper;
import com.ecommerce.model.cart.Cart;
import com.ecommerce.model.cart.CartItem;
import com.ecommerce.model.order.Order;
import com.ecommerce.model.order.OrderItem;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.order.OrderRepository;
import com.ecommerce.repository.user.ClientRepository;
import com.ecommerce.util.AuthenticateClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    private final OrderMapper orderMapper;
    private final AuthenticateClient authenticateClient;


    @Transactional
    public OrderDto createOrderFromCart() {

        Client client = authenticateClient.getAuthenticatedClient();

        Cart cart = client.getCart();

        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

//        initial hashset
        Set<OrderItem> orderItems = new HashSet<>();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .plant(cartItem.getPlant())
                    .quantity(cartItem.getQuantity())
                    .priceAtPurchase(cartItem.getSubtotal())
                    .build();
            orderItems.add(orderItem);
        }

        Order order = Order.builder()
                .client(client)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.NEW)
                .totalPrice(cart.getTotalPrice())
                .orderItems(orderItems)
                .shippingAddress(client.getShippingAddress())
                .build();

        orderItems.forEach(item->item.setOrder(order));
        Order savedOrder = orderRepository.save(order);
        cart.getCartItems().clear();

        return orderMapper.orderToOrderDto(savedOrder);
    }

    public List<OrderDto> findAllUserOrders () {

        Client client = authenticateClient.getAuthenticatedClient();

        List<Order> orders = orderRepository.findByClientId(client.getId());

        return orders.stream()
                .map(orderMapper :: orderToOrderDto)
                .toList();
    }


    public List<OrderDto> findAllOrders() {
        List<Order> allOrders = orderRepository.findAll();
        return allOrders.stream()
                .map(orderMapper::orderToOrderDto)
                .collect(Collectors.toList());
    }


    public OrderDto updateStatus (Long orderId, OrderStatus newOrderStatus) {
        Order orderToUpdate = orderRepository.findById(orderId)
                .orElseThrow(()->new IllegalStateException("Order with this id does not exist"));

        orderToUpdate.setStatus(newOrderStatus);

        orderRepository.save(orderToUpdate);

        return orderMapper.orderToOrderDto(orderToUpdate);
    }
}