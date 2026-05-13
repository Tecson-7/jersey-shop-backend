package com.jerseyshop.jersey_shop_backend.service;

import com.jerseyshop.jersey_shop_backend.dto.CartItemRequest;
import com.jerseyshop.jersey_shop_backend.dto.CheckoutRequest;
import com.jerseyshop.jersey_shop_backend.model.*;
import com.jerseyshop.jersey_shop_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Cart cart = Cart.builder()
                    .user(user)
                    .cartItems(new ArrayList<>())
                    .build();
            return cartRepository.save(cart);
        });
    }

    public Cart addToCart(Long userId, CartItemRequest request) {
        Cart cart = getOrCreateCart(userId);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + request.getQuantity());
                cartItemRepository.save(item);
                return cartRepository.save(cart);
            }
        }

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(request.getQuantity())
                .build();

        cart.getCartItems().add(cartItem);
        return cartRepository.save(cart);
    }

    public Cart getCart(Long userId) {
        return getOrCreateCart(userId);
    }

    public Cart updateCartItem(Long userId, Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        return getCart(userId);
    }

    public Cart removeCartItem(Long userId, Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        return getCart(userId);
    }

    public Order checkout(Long userId, CheckoutRequest checkoutRequest) {
        Cart cart = getOrCreateCart(userId);

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0;

        Order order = Order.builder()
                .user(cart.getUser())
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .orderItems(orderItems)
                .totalPrice(0.0)
                .fullName(checkoutRequest.getFullName())
                .phone(checkoutRequest.getPhone())
                .address(checkoutRequest.getAddress())
                .city(checkoutRequest.getCity())
                .state(checkoutRequest.getState())
                .pincode(checkoutRequest.getPincode())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + product.getName());
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .build();

            orderItems.add(orderItem);
            totalPrice += product.getPrice() * cartItem.getQuantity();
        }

        savedOrder.setOrderItems(orderItems);
        savedOrder.setTotalPrice(totalPrice);
        orderRepository.save(savedOrder);

        cart.getCartItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }
}