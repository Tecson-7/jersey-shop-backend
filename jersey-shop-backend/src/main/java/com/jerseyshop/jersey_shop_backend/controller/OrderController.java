package com.jerseyshop.jersey_shop_backend.controller;

import com.jerseyshop.jersey_shop_backend.model.Order;
import com.jerseyshop.jersey_shop_backend.model.OrderStatus;
import com.jerseyshop.jersey_shop_backend.model.PaymentStatus;
import com.jerseyshop.jersey_shop_backend.repository.UserRepository;
import com.jerseyshop.jersey_shop_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    private Long getUserId(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    // Customer — view own orders
    @GetMapping("/api/orders/my-orders")
    public ResponseEntity<List<Order>> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.getMyOrders(getUserId(userDetails)));
    }

    // Customer — view single order
    @GetMapping("/api/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // Admin — view all orders
    @GetMapping("/api/admin/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // Admin — update order status
    @PutMapping("/api/admin/orders/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id,
                                                   @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    // Admin — update payment status
    @PutMapping("/api/admin/orders/{id}/payment-status")
    public ResponseEntity<Order> updatePaymentStatus(@PathVariable Long id,
                                                     @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(orderService.updatePaymentStatus(id, status));
    }
}