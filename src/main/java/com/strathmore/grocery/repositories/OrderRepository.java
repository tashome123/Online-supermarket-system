package com.strathmore.grocery.repositories;

public interface OrderRepository extends JpaRepository<CartItem,Long> {
  Page<CartItem> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
