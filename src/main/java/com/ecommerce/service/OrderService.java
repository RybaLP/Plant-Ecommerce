package com.ecommerce.service;

import com.ecommerce.dto.cart.CartItemDto;
import com.ecommerce.dto.order.CreateGuestOrderDto;
import com.ecommerce.dto.order.CreateUserOrder;
import com.ecommerce.dto.order.OrderDto;
import com.ecommerce.dto.payment.CheckoutResponseDto;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.mapper.OrderMapper;
import com.ecommerce.model.cart.Cart;
import com.ecommerce.model.cart.CartItem;
import com.ecommerce.model.order.Order;
import com.ecommerce.model.order.OrderItem;
import com.ecommerce.model.product.Plant;
import com.ecommerce.model.user.Address;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.order.OrderRepository;
import com.ecommerce.repository.plant.PlantRepository;
import com.ecommerce.repository.user.AddressRepository;
import com.ecommerce.util.AuthenticateClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AuthenticateClient authenticateClient;
    private final PaymentService paymentService;
    private final PlantRepository plantRepository;
    private final AddressRepository addressRepository;


    @Transactional
    public CheckoutResponseDto createOrderFromCart(CreateUserOrder createUserOrder) {

        Client client = authenticateClient.getAuthenticatedClient();
        Cart cart = client.getCart();

        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        BigDecimal itemsPrice = cart.getCartItems().stream()
                .map(CartItem :: getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

//        calculate total price including delivery
        BigDecimal totalPrice = itemsPrice.add(createUserOrder.getDeliveryPrice());

//        initial hashset
        Set<OrderItem> orderItems = new HashSet<>();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .plant(cartItem.getPlant())
                    .quantity(cartItem.getQuantity())
                    .priceAtPurchase(cartItem.getPlant().getPrice())
                    .build();
            orderItems.add(orderItem);
        }

        Order order = Order.builder()
                .client(client)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.NEW)
                .totalPrice(totalPrice)
                .orderItems(orderItems)
                .paymentMethod(createUserOrder.getPaymentMethod())
                .deliveryPrice(createUserOrder.getDeliveryPrice())
                .shippingAddress(client.getShippingAddress())
                .nip(createUserOrder.getNip())
                .companyName(createUserOrder.getCompanyName())
                .isCompanyOrder(createUserOrder.isCompanyOrder())
                .build();

        orderItems.forEach(item->item.setOrder(order));
        order.setOrderNumber(order.generateOrderNumber());

        if (order.isCompanyOrder() && (order.getNip() == null || order.getCompanyName() == null)) {
            throw new IllegalStateException("You have to correct nip or company name");
        }
        cart.getCartItems().clear();

        orderRepository.save(order);

        switch (order.getPaymentMethod()) {
            case STRIPE -> {
                CheckoutResponseDto checkoutResponseDto = paymentService.createCheckoutSession(order.getId(), order.getOrderNumber());
                return CheckoutResponseDto.builder()
                        .orderNumber(order.getOrderNumber())
                        .stripeCheckoutUrl(checkoutResponseDto.getStripeCheckoutUrl())
                        .build();
            }

            case CASH_ON_DELIVERY -> {
                order.setStatus(OrderStatus.PROCESSING);
                orderRepository.save(order);
                return CheckoutResponseDto.builder()
                        .orderNumber(order.getOrderNumber())
                        .build();
            }

            default -> throw new IllegalArgumentException("Unsupported payment method");
        }
    }


    @Transactional
    public CheckoutResponseDto createGuestOrder (CreateGuestOrderDto createGuestOrderDto) {

        Address address = Address.builder()
                .street(createGuestOrderDto.getShippingAddress().getStreet())
                .city(createGuestOrderDto.getShippingAddress().getCity())
                .country(createGuestOrderDto.getShippingAddress().getCountry())
                .postalCode(createGuestOrderDto.getShippingAddress().getPostalCode())
                .build();

        addressRepository.save(address);

        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal totalItemsPrice = BigDecimal.ZERO;

        for (CartItemDto cartItemDto : createGuestOrderDto.getCartItems()) {

            Plant plant = plantRepository.findById(cartItemDto.getPlant().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Plant not found: " + cartItemDto.getPlant().getId()));


            OrderItem orderItem = OrderItem.builder()
                    .plant(plant)
                    .quantity(cartItemDto.getQuantity())
                    .priceAtPurchase(cartItemDto.getPlant().getPrice())
                    .build();

            orderItems.add(orderItem);


            totalItemsPrice = totalItemsPrice.add(orderItem.getPriceAtPurchase().multiply(
                    BigDecimal.valueOf(orderItem.getQuantity())
            ));
        }

        BigDecimal deliveryPrice = createGuestOrderDto.getDeliveryPrice();
        BigDecimal totalPrice = totalItemsPrice.add(deliveryPrice);

        Order order = Order.builder()
                .guestFirstName(createGuestOrderDto.getGuestFirstName())
                .guestLastName(createGuestOrderDto.getGuestLastName())
                .guestEmail(createGuestOrderDto.getGuestEmail())
                .guestPhone(createGuestOrderDto.getGuestPhone())
                .shippingAddress(address)
                .paymentMethod(createGuestOrderDto.getPaymentMethod())
                .deliveryPrice(deliveryPrice)
                .totalPrice(totalPrice)
                .status(OrderStatus.NEW)
                .orderDate(LocalDateTime.now())
                .orderItems(orderItems)
                .isCompanyOrder(createGuestOrderDto.isCompanyOrder())
                .nip(createGuestOrderDto.getNip())
                .companyName(createGuestOrderDto.getCompanyName())
                .build();

        order.setOrderNumber(order.generateOrderNumber());
        orderItems.forEach(item -> item.setOrder(order));


        if (order.isCompanyOrder()) {
            if (order.getNip() == null || order.getCompanyName() == null) {
                throw new IllegalStateException("NIP and Company name must be provided!");
            }
        }

        orderRepository.save(order);


        switch (order.getPaymentMethod()) {
            case STRIPE -> {
                CheckoutResponseDto checkoutResponseDto = paymentService.createCheckoutSession(order.getId() , order.getOrderNumber());
                return CheckoutResponseDto.builder()
                        .orderNumber(order.getOrderNumber())
                        .stripeCheckoutUrl(checkoutResponseDto.getStripeCheckoutUrl())
                        .build();
            }
            case CASH_ON_DELIVERY -> {
                order.setStatus(OrderStatus.PROCESSING);
                orderRepository.save(order);
                return CheckoutResponseDto.builder()
                        .orderNumber(order.getOrderNumber())
                        .build();
            }

            default -> throw new IllegalArgumentException("Unsupported paymend method");
        }
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

    public boolean isOrderNumberValid(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber);
        return order != null;
    }
}