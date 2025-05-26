package com.strathmore.grocery.repositories;


  public interface CartItemRepository extends JpaRepository<CartItem,Long> {
  Page<CartItem> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

