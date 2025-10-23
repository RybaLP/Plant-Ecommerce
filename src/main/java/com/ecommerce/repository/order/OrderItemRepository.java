package com.ecommerce.repository.order;

import com.ecommerce.model.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
            SELECT oi FROM OrderItem oi
            WHERE oi.order.client.id = :clientId
            AND oi.plant.id = :plantId
            """)

    List<OrderItem> findAllByClientAndPlantId(
            @Param("clientId") Long clientId,
            @Param("plantId") Long plantId
    );
}