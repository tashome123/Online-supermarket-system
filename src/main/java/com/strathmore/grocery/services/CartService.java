package com.strathmore.grocery.services;

import com.strathmore.grocery.models.CartItem;
import com.strathmore.grocery.models.Product;
import com.strathmore.grocery.models.User;
import com.strathmore.grocery.repositories.CartItemRepository;
import com.strathmore.grocery.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    
    @Autowired
    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }
    
    public List<CartItem> getUserCart(User user) {
        return cartItemRepository.findByUser(user);
    }
    
    public CartItem addToCart(User user, Long productId, Integer quantity) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (!productOpt.isPresent()) {
            throw new RuntimeException("Product not found");
        }
        
        Product product = productOpt.get();
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        Optional<CartItem> existingItem = cartItemRepository.findByUserAndProductId(user, productId);
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setUnitPrice(product.getPrice());
            return cartItemRepository.save(newItem);
        }
    }
    
    public CartItem updateCartItemQuantity(Long cartItemId, Integer quantity) {
        Optional<CartItem> cartItemOpt = cartItemRepository.findById(cartItemId);
        if (!cartItemOpt.isPresent()) {
            throw new RuntimeException("Cart item not found");
        }
        
        CartItem cartItem = cartItemOpt.get();
        if (cartItem.getProduct().getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }
    
    public void removeFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
    
    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }
    
    public BigDecimal getCartTotal(User user) {
        List<CartItem> cartItems = getUserCart(user);
        return cartItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
