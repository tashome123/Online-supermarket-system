package com.strathmore.grocery.controllers;

import com.strathmore.grocery.models.CartItem;
import com.strathmore.grocery.models.User;
import com.strathmore.grocery.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(@AuthenticationPrincipal User user, Model model) {
        List<CartItem> cartItems = cartService.getUserCart(user);
        BigDecimal total = cartService.getCartTotal(user);
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart/view";
    }

    @PostMapping("/add/{productId}")
    @ResponseBody
    public String addToCart(@AuthenticationPrincipal User user, 
                           @PathVariable Long productId, 
                           @RequestParam(defaultValue = "1") Integer quantity) {
        try {
            cartService.addToCart(user, productId, quantity);
            return "Product added to cart successfully!";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/update/{cartItemId}")
    @ResponseBody
    public String updateQuantity(@PathVariable Long cartItemId, 
                                @RequestParam Integer quantity) {
        try {
            cartService.updateCartItemQuantity(cartItemId, quantity);
            return "Cart updated successfully!";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/remove/{cartItemId}")
    @ResponseBody
    public String removeFromCart(@PathVariable Long cartItemId) {
        try {
            cartService.removeFromCart(cartItemId);
            return "Item removed from cart successfully!";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/clear")
    @ResponseBody
    public String clearCart(@AuthenticationPrincipal User user) {
        try {
            cartService.clearCart(user);
            return "Cart cleared successfully!";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }
}
