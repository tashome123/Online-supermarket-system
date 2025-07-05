package com.strathmore.grocery.controllers;

import com.strathmore.grocery.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    
    private final ProductService productService;
    
    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping("/")
    public String home(Model model) {
        List<String> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);
        return "home";
    }
    
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
} 