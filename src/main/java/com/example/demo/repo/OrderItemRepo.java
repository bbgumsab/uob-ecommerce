package com.example.demo.repo;


import com.example.demo.models.OrderItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi " +
            "FROM OrderItem oi " +
            "WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderId(Long orderId);
}

