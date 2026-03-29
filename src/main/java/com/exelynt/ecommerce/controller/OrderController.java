package com.exelynt.ecommerce.controller;

import com.exelynt.ecommerce.model.*;
import com.exelynt.ecommerce.repository.*;
import com.exelynt.ecommerce.service.OrderService;
import com.exelynt.ecommerce.service.StripeService;
import jakarta.persistence.EntityNotFoundException;
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
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> placeOrder(@RequestParam String address) {
        User user = getAuthenticatedUser();
        Order order = orderService.createOrderFromCart(user, address);
        
        String paymentUrl = stripeService.createCheckoutSession(order.getTotalPrice());

        return ResponseEntity.ok(Map.of(
            "message", "Order initiated successfully",
            "orderId", order.getId(),
            "paymentUrl", paymentUrl,
            "totalAmount", order.getTotalPrice(),
            "status", order.getPaymentStatus()
        ));
    }

    @PutMapping("/{id}/confirm-payment")
    public ResponseEntity<?> confirmPayment(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        
        User user = getAuthenticatedUser();
        if (!order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Unauthorized: You can only confirm your own orders.");
        }

        order.setPaymentStatus("PAID");
        orderRepository.save(order);

        return ResponseEntity.ok(Map.of(
            "message", "Payment confirmed. Order status updated to PAID",
            "orderId", id
        ));
    }

   
    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders() {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(orderRepository.findByUser(user));
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderDetails(@PathVariable Long id) {
        User user = getAuthenticatedUser();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access Denied: Not your order.");
        }
        return ResponseEntity.ok(order);
    }
}