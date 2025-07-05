package com.strathmore.grocery.controllers;

import com.strathmore.grocery.models.Order;
import com.strathmore.grocery.models.User;
import com.strathmore.grocery.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getUserOrders(@AuthenticationPrincipal User user, Model model) {
        List<Order> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String getOrderDetails(@PathVariable Long id, 
                                 @AuthenticationPrincipal User user, 
                                 Model model) {
        Order order = orderService.getOrderById(id);
        if (order != null && order.getUser().getId().equals(user.getId())) {
            model.addAttribute("order", order);
            return "orders/detail";
        }
        return "redirect:/orders";
    }

    @PostMapping("/create")
    @ResponseBody
    public String createOrder(@AuthenticationPrincipal User user, 
                             @RequestParam String shippingAddress,
                             @RequestParam String phoneNumber) {
        try {
            Order order = orderService.createOrder(user, shippingAddress, phoneNumber);
            return "Order created successfully! Order ID: " + order.getId();
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/{id}/cancel")
    @ResponseBody
    public String cancelOrder(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            orderService.cancelOrder(id, user);
            return "Order cancelled successfully!";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }
}

