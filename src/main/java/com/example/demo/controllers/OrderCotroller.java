package com.example.demo.controllers;

import java.util.HashSet;
import java.util.List;

import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.validation.BindingResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.example.demo.dto.OrderSummary;
import com.example.demo.models.Order;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.OrderRepo;

import com.example.demo.services.UserService;

@Controller
@RequestMapping("/orders")
public class OrderCotroller {

    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final UserService userService;

    @Autowired
    public OrderCotroller(ProductRepo productRepo, OrderRepo orderRepo, UserService userService) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.userService = userService;
    }

    @GetMapping()
    public String listOrders(Model model) {
        List<OrderSummary> orderSummaries = orderRepo.findOrderSummaries();
        model.addAttribute("orderSummaries", orderSummaries);
        System.out.println(orderSummaries);
        return "orders/index";
    }
}
