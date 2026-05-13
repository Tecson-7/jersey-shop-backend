package com.jerseyshop.jersey_shop_backend.dto;

import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private String description;
}