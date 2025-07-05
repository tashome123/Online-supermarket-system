package com.strathmore.grocery.controllers;

import com.strathmore.grocery.models.User;
import com.strathmore.grocery.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {
    
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    
    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String showRegisterForm() {
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/auth/login?registered=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid email or password");
            return "auth/login";
        }
    }
    
    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/auth/login";
    }
    
    // API endpoints for AJAX calls
    @PostMapping("/api/register")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> registerUserApi(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            User registeredUser = userService.registerUser(user);
            response.put("success", true);
            response.put("message", "User registered successfully!");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}