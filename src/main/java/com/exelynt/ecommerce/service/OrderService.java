package com.exelynt.ecommerce.service;

import com.exelynt.ecommerce.model.*;
import com.exelynt.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Order createOrderFromCart(User user, String address) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(address);
        
        List<Product> products = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            products.add(item.getProduct());
           
            BigDecimal itemPrice = item.getProduct().getPrice(); 
            BigDecimal quantity = new BigDecimal(item.getQuantity());
            total = total.add(itemPrice.multiply(quantity));
        }

        order.setProducts(products);
        order.setTotalPrice(total);
        order.setPaymentStatus("PENDING");

        Order savedOrder = orderRepository.save(order);
        
        cartItemRepository.deleteAll(cartItems);
        
        return savedOrder;
    }

    
    public Order updatePaymentStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setPaymentStatus(status);
        return orderRepository.save(order);
    }
}