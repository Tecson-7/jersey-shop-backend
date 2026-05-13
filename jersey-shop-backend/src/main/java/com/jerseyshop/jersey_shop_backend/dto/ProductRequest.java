package com.jerseyshop.jersey_shop_backend.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String size;
    private String imageUrl;
    private Long categoryId;
}