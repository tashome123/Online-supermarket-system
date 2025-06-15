package com.strathmore.grocery.repositories;

 public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);
}

