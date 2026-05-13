package com.jerseyshop.jersey_shop_backend.repository;

import com.jerseyshop.jersey_shop_backend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}