package com.strathmore.grocery.controllers;

import com.strathmore.grocery.models.Product;
import com.strathmore.grocery.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    private final ProductService productService;
    
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        System.out.println("Products found: " + products.size());
        if (!products.isEmpty()) {
            System.out.println("First product: " + products.get(0).toString());
        }
        
        List<String> categories = productService.getAllCategories();
        System.out.println("Categories found: " + categories);
        
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "products/list";
    }
    
    @GetMapping("/category/{category}")
    public String getProductsByCategory(@PathVariable String category, Model model) {
        List<Product> products = productService.getProductsByCategory(category);
        List<String> categories = productService.getAllCategories();
        
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category);
        return "products/list";
    }
    
    @GetMapping("/search")
    public String searchProducts(@RequestParam String keyword, Model model) {
        List<Product> products = productService.searchProducts(keyword);
        List<String> categories = productService.getAllCategories();
        
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("searchKeyword", keyword);
        return "products/list";
    }
    
    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "products/detail";
                })
                .orElse("redirect:/products");
    }
    
    // Admin endpoints
    @GetMapping("/admin")
    public String adminProductList(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/products/list";
    }
    
    @GetMapping("/admin/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/products/form";
    }
    
    @PostMapping("/admin")
    public String createProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products/admin";
    }
    
    @GetMapping("/admin/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "admin/products/form";
                })
                .orElse("redirect:/products/admin");
    }
    
    @PostMapping("/admin/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product) {
        product.setId(id);
        productService.saveProduct(product);
        return "redirect:/products/admin";
    }
    
    @PostMapping("/admin/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products/admin";
    }
}