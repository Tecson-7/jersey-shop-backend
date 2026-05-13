package com.jerseyshop.jersey_shop_backend.repository;

import com.jerseyshop.jersey_shop_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}