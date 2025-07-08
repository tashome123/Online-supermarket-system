package com.strathmore.grocery.services;

import com.strathmore.grocery.models.CartItem;
import com.strathmore.grocery.models.Product;
import com.strathmore.grocery.models.User;
import com.strathmore.grocery.repositories.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CartService {
    
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    
    @Autowired
    public CartService(CartItemRepository cartItemRepository, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }
    
    public List<CartItem> getUserCart(User user) {
        return cartItemRepository.findByUser(user);
    }
    
    public void addToCart(User user, Long productId, Integer quantity) {
        Product product = productService.getProductById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
            
        CartItem existingItem = cartItemRepository.findByUserAndProduct(user, product)
            .orElse(null);
            
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setUnitPrice(product.getPrice());
            cartItemRepository.save(newItem);
        }
    }
    
    public void updateCartItemQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeFromCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Optional: Add security check to ensure the item belongs to the current user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!cartItem.getUser().getEmail().equals(auth.getName())) {
            throw new RuntimeException("Unauthorized access to cart item");
        }

        cartItemRepository.delete(cartItem);
    }


    public void clearCart(User user) {
        List<CartItem> userItems = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(userItems);
    }
    
    public BigDecimal getCartTotal(User user) {
        return getUserCart(user).stream()
            .map(item -> item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}