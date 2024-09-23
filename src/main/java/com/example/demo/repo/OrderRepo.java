package com.example.demo.repo;

import com.example.demo.dto.OrderSummary;
import com.example.demo.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    @Query("SELECT new com.example.demo.dto.OrderSummary(o.id, o.user, o.status, o.createdAt, SUM(oi.price * oi.quantity)) "
            +
            "FROM Order o " +
            "JOIN OrderItem oi ON o.id = oi.order.id " +
            "GROUP BY o.id, o.user, o.status, o.createdAt")
    List<OrderSummary> findOrderSummaries();

}