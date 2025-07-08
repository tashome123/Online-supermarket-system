package com.strathmore.grocery.controllers;

import com.strathmore.grocery.models.CartItem;
import com.strathmore.grocery.models.User;
import com.strathmore.grocery.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String checkout(@AuthenticationPrincipal User user, Model model) {
        List<CartItem> cartItems = cartService.getUserCart(user);
        BigDecimal total = cartService.getCartTotal(user);
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "checkout/index";
    }
}