package com.strathmore.grocery.services;

import com.strathmore.grocery.models.CartItem;
import com.strathmore.grocery.models.Order;
import com.strathmore.grocery.models.OrderItem;
import com.strathmore.grocery.models.User;
import com.strathmore.grocery.repositories.OrderRepository;
import com.strathmore.grocery.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final CartService cartService;
    
    @Autowired
    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public Order createOrder(User user, String shippingAddress, String phoneNumber) {
        List<CartItem> cartItems = cartService.getUserCart(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setShippingAddress(shippingAddress);
        order.setPhoneNumber(phoneNumber);

        // Calculate total and create order items
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            total = total.add(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        // Clear the cart after order creation
        cartService.clearCart(user);

        return savedOrder;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElse(null);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }

    public void cancelOrder(Long orderId, User user) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (order.getUser().getId().equals(user.getId()) && 
                "PENDING".equals(order.getStatus())) {
                order.setStatus("CANCELLED");
                orderRepository.save(order);
            } else {
                throw new RuntimeException("Order cannot be cancelled");
            }
        } else {
            throw new RuntimeException("Order not found");
        }
    }
}
