package com.exelynt.ecommerce.service;

import com.exelynt.ecommerce.model.*;
import com.exelynt.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, 
                        CartItemRepository cartItemRepository, 
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrderFromCart(User user, String address) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        double total = 0;
        List<Product> products = new ArrayList<>();

        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + product.getName());
            }

            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);

            products.add(product);
            total += product.getPrice() * item.getQuantity();
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(address);
        order.setTotalPrice(total);
        order.setProducts(products);
        order.setPaymentStatus("PENDING");

        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteByUser(user);
        
        return savedOrder;
    }

    public Order updatePaymentStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Order not found"));
        order.setPaymentStatus(status);
        return orderRepository.save(order);
    }
}