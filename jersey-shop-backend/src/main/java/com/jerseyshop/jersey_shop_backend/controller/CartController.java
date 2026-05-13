package com.jerseyshop.jersey_shop_backend.controller;

import com.jerseyshop.jersey_shop_backend.dto.CartItemRequest;
import com.jerseyshop.jersey_shop_backend.dto.CheckoutRequest;
import com.jerseyshop.jersey_shop_backend.model.Cart;
import com.jerseyshop.jersey_shop_backend.model.Order;
import com.jerseyshop.jersey_shop_backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.jerseyshop.jersey_shop_backend.repository.UserRepository;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    private Long getUserId(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addToCart(getUserId(userDetails), request));
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.getCart(getUserId(userDetails)));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok("Hello " + userDetails.getUsername());
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<Cart> updateCartItem(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable Long cartItemId,
                                               @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.updateCartItem(getUserId(userDetails), cartItemId, quantity));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Cart> removeCartItem(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.removeCartItem(getUserId(userDetails), cartItemId));
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestBody CheckoutRequest checkoutRequest) {
        try {
            Long userId = getUserId(userDetails);
            Order order = cartService.checkout(userId, checkoutRequest);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}