package com.strathmore.grocery.config;

import com.strathmore.grocery.models.Product;
import com.strathmore.grocery.models.User;
import com.strathmore.grocery.repositories.ProductRepository;
import com.strathmore.grocery.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user if not exists
        if (!userRepository.existsByEmail("admin@supermarket.com")) {
            User admin = new User();
            admin.setEmail("admin@supermarket.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("Admin User");
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
        }

        // Create sample products if none exist
        if (productRepository.count() == 0) {
            createSampleProducts();
        }
    }

    private void createSampleProducts() {
        List<Product> products = Arrays.asList(
            // Fruits
            createProduct("Fresh Apples", "Sweet and juicy red apples", new BigDecimal("200"), 50, "Fruits"),
                createProduct("Bananas", "Fresh yellow bananas", new BigDecimal("230"), 100, "Fruits"),
            createProduct("Oranges", "Sweet and tangy oranges", new BigDecimal("50"), 75, "Fruits"),
            createProduct("Strawberries", "Fresh red strawberries", new BigDecimal("150"), 30, "Fruits"),

            // Vegetables
            createProduct("Fresh Tomatoes", "Ripe red tomatoes", new BigDecimal("30"), 60, "Vegetables"),
            createProduct("Carrots", "Fresh orange carrots", new BigDecimal("180"), 80, "Vegetables"),
            createProduct("Broccoli", "Fresh green broccoli", new BigDecimal("250"), 40, "Vegetables"),
            createProduct("Spinach", "Fresh spinach leaves", new BigDecimal("200"), 45, "Vegetables"),

            // Dairy
            createProduct("Whole Milk", "Fresh whole milk 1L", new BigDecimal("280"), 25, "Dairy"),
            createProduct("Cheddar Cheese", "Aged cheddar cheese 250g", new BigDecimal("500"), 35, "Dairy"),
            createProduct("Greek Yogurt", "Plain Greek yogurt 500g", new BigDecimal("400"), 30, "Dairy"),
            createProduct("Butter", "Unsalted butter 250g", new BigDecimal("350"), 40, "Dairy"),

            // Bakery
            createProduct("Whole Wheat Bread", "Fresh whole wheat bread", new BigDecimal("100"), 20, "Bakery"),
            createProduct("Croissants", "Buttery croissants 6-pack", new BigDecimal("250"), 15, "Bakery"),
            createProduct("Chocolate Cake", "Delicious chocolate cake", new BigDecimal("1500"), 10, "Bakery"),
            createProduct("Bagels", "Fresh bagels 6-pack", new BigDecimal("450"), 25, "Bakery"),

            // Meat
            createProduct("Chicken Breast", "Fresh chicken breast 500g", new BigDecimal("550"), 20, "Meat"),
            createProduct("Ground Beef", "Lean ground beef 500g", new BigDecimal("850"), 30, "Meat"),
            createProduct("Salmon Fillet", "Fresh salmon fillet 300g", new BigDecimal("750"), 15, "Meat"),
            createProduct("Pork Chops", "Fresh pork chops 400g", new BigDecimal("1100"), 25, "Meat"),

            // Beverages
            createProduct("Orange Juice", "Fresh orange juice 1L", new BigDecimal("350"), 40, "Beverages"),
            createProduct("Coffee Beans", "Premium coffee beans 250g", new BigDecimal("1300"), 35, "Beverages"),
            createProduct("Green Tea", "Organic green tea 50 bags", new BigDecimal("600"), 50, "Beverages"),
            createProduct("Sparkling Water", "Sparkling water 6-pack", new BigDecimal("400"), 60, "Beverages"),

            // Snacks
            createProduct("Potato Chips", "Classic potato chips 200g", new BigDecimal("150"), 45, "Snacks"),
            createProduct("Mixed Nuts", "Premium mixed nuts 300g", new BigDecimal("750"), 30, "Snacks"),
            createProduct("Dark Chocolate", "70% dark chocolate 100g", new BigDecimal("500"), 40, "Snacks"),
            createProduct("Popcorn", "Microwave popcorn 3-pack", new BigDecimal("300"), 55, "Snacks")
        );

        productRepository.saveAll(products);
    }

    private Product createProduct(String name, String description, BigDecimal price, Integer stock, String category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stock);
        product.setCategory(category);
        product.setActive(true);
        return product;
    }
}