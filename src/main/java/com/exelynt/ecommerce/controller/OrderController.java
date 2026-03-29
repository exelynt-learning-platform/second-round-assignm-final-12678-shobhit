package com.exelynt.ecommerce.controller;

import com.exelynt.ecommerce.model.*;
import com.exelynt.ecommerce.repository.*;
import com.exelynt.ecommerce.service.OrderService;
import com.exelynt.ecommerce.service.StripeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StripeService stripeService;

    public OrderController(OrderService orderService, OrderRepository orderRepository, 
                           UserRepository userRepository, StripeService stripeService) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.stripeService = stripeService;
    }

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> placeOrder(@RequestParam String address) {
        try {
            User user = getAuthenticatedUser();
            Order order = orderService.createOrderFromCart(user, address);
            
            // Now calling with BigDecimal - NO ERROR
            String paymentUrl = stripeService.createCheckoutSession(order.getTotalPrice());

            return ResponseEntity.ok(Map.of(
                "message", "Order initiated successfully",
                "orderId", order.getId(),
                "paymentUrl", paymentUrl,
                "totalAmount", order.getTotalPrice()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders() {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(orderRepository.findByUser(user));
    }
}