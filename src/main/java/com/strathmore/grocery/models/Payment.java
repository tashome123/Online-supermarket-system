package com.strathmore.grocery.models;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;  
    private String paymentMethod;
    private Double totalamount;
    private LocalDate paymentDate;

    
}
