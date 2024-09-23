package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.OrderSummary;
import com.example.demo.models.Order;
import com.example.demo.models.OrderItem;
import com.example.demo.models.User;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.OrderRepo;
import com.example.demo.repo.OrderItemRepo;

import com.example.demo.services.UserService;
import com.example.demo.services.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderCotroller {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final OrderService orderService;

    @Autowired
    public OrderCotroller(ProductRepo productRepo, OrderRepo orderRepo, OrderItemRepo orderItemRepo,
            UserService userService, OrderService orderService) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.orderService = orderService;
    }

    @GetMapping()
    public String listOrders(Model model) {
        List<OrderSummary> orderSummaries = orderRepo.findOrderSummaries();
        model.addAttribute("orderSummaries", orderSummaries);
        return "orders/index";
    }

    @GetMapping("/{id}")
    public String orderDetails(@PathVariable Long id, Model model) {
        var order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        var orderItems = orderItemRepo.findByOrderId(id);
        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        return "orders/details";
    }

    @GetMapping("/{id}/delete")
    public String showDeleteOrderForm(@PathVariable Long id, Model model) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        model.addAttribute("order", order);
        return "orders/delete";
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderRepo.deleteById(id);
        return "redirect:/orders";
    }
    // The following SQL query can be used to create a foreign key constraint
    // between the orders and order_items tables:
    // ALTER TABLE order_items
    // ADD CONSTRAINT FK_order
    // FOREIGN KEY (order_id)
    // REFERENCES orders(id)
    // ON DELETE CASCADE;

    @PostMapping("/{id}/edit_status")
    public String editStatus(@PathVariable Long id,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {
        try {
            orderService.editStatus(id, status);

            redirectAttributes.addFlashAttribute("message",
                    String.format("Status updated to %s", status));

            return "redirect:/orders";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders";
        }
    }
}
