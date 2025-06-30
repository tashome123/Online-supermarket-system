package com.strathmore.grocery.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userid;
    private LocalDate orderDate;
    private Double totalAmount;

    
}

