package com.ecommerce.controller;

import com.ecommerce.dto.order.*;
import com.ecommerce.dto.payment.CheckoutResponseDto;
import com.ecommerce.enums.CourierType;
import com.ecommerce.repository.order.OrderRepository;
import com.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CheckoutResponseDto> createOrder (@Valid @RequestBody CreateUserOrder createUserOrder) {
        CheckoutResponseDto checkoutResponseDto = orderService.createOrderFromCart(createUserOrder);
        return ResponseEntity.ok(checkoutResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAllOrdersFromClient () {
        return ResponseEntity.ok(orderService.findAllUserOrders());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderDto> ediotOrderStatus (@PathVariable("id") Long id, @Valid @RequestBody UpdateOrderStatusDto updateOrderStatusDto) {
        return ResponseEntity.ok(orderService.updateStatus(id, updateOrderStatusDto.getOrderStatus()));
    }

    @PostMapping("/guest")
    public ResponseEntity<CheckoutResponseDto> createGuestOrder (@RequestBody CreateGuestOrderDto createGuestOrderDto) {
        return ResponseEntity.ok(orderService.createGuestOrder(createGuestOrderDto));
    }

    @PostMapping("/check")
    public ResponseEntity<Void> isOrderNumberValid(@RequestParam String orderNumber) {
        boolean valid = orderService.isOrderNumberValid(orderNumber);
        if (valid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}