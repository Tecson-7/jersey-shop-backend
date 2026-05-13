package com.jerseyshop.jersey_shop_backend.repository;

import com.jerseyshop.jersey_shop_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}