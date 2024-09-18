package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.example.demo.models.User;

public class OrderSummary {
    private Long orderId;
    private User user;
    private String status;
    private Date createdAt;
    private BigDecimal totalPrice;

    public OrderSummary(Long orderId, User user, String status, Date createdAt, BigDecimal totalPrice) {
        this.orderId = orderId;
        this.user = user;
        this.status = status;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
    }

    // Getters
    public Long getOrderId() {
        return orderId;
    }

    public User getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        var userId = user.getId();
        return "OrderSummary{" +
                "orderId=" + orderId +
                ", userId=" + userId + 
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", totalPrice=" + totalPrice +
                '}';
    }
}