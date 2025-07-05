package com.strathmore.grocery.services;

import com.strathmore.grocery.models.Order;
import com.strathmore.grocery.models.Payment;
import com.strathmore.grocery.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    
    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(Order order, String paymentMethod, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("PENDING");
        
        return paymentRepository.save(payment);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElse(null);
    }

    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.findByStatus(status);
    }

    public List<Payment> getPaymentsByMethod(String paymentMethod) {
        return paymentRepository.findByPaymentMethod(paymentMethod);
    }

    public Payment processPayment(Long paymentId, String transactionId) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus("COMPLETED");
            payment.setTransactionId(transactionId);
            return paymentRepository.save(payment);
        }
        return null;
    }

    public Payment refundPayment(Long paymentId, String reason) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus("REFUNDED");
            payment.setPaymentDetails("Refund reason: " + reason);
            return paymentRepository.save(payment);
        }
        return null;
    }
}

