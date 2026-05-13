package com.jerseyshop.jersey_shop_backend.dto;

import com.jerseyshop.jersey_shop_backend.model.PaymentMethod;
import lombok.Data;

@Data
public class CheckoutRequest {
    private String fullName;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private PaymentMethod paymentMethod;
}