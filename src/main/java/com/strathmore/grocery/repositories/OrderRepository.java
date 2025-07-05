package com.strathmore.grocery.repositories;

import com.strathmore.grocery.models.Order;
import com.strathmore.grocery.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByUser(User user);
    
    List<Order> findByStatus(String status);
    
    List<Order> findByUserOrderByOrderDateDesc(User user);
}

