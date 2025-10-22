package com.ecommerce.repository.order;

import com.ecommerce.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByClientId(Long clientId);

    boolean existsByClientIdAndOrderItemsPlantId(Long clientId, Long plantId);

    Order findByOrderNumber(String orderNumber);
}
