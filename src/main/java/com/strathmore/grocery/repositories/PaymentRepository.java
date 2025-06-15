package com.strathmore.grocery.repositories;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findByPaymentMethodContainingIgnoreCase(String paymentMethod, Pageable pageable);
}

