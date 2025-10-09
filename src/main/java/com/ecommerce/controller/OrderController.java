package com.ecommerce.controller;

import com.ecommerce.dto.order.ClientIdDto;
import com.ecommerce.dto.order.OrderDto;
import com.ecommerce.dto.order.UpdateOrderStatusDto;
import com.ecommerce.model.order.Order;
import com.ecommerce.repository.order.OrderRepository;
import com.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder () {
        OrderDto newOrder = orderService.createOrderFromCart();
        return ResponseEntity.status((HttpStatus.CREATED)).body(newOrder);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAllOrdersFromClient () {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderDto> ediotOrderStatus (@PathVariable("id") Long id, @Valid @RequestBody UpdateOrderStatusDto updateOrderStatusDto) {
        return ResponseEntity.ok(orderService.updateStatus(id, updateOrderStatusDto.getOrderStatus()));
    }
}