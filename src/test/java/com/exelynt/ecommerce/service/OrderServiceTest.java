package com.exelynt.ecommerce.service;

import com.exelynt.ecommerce.model.*;
import com.exelynt.ecommerce.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("shobhit@example.com");

        product = new Product();
        product.setId(101L);
        product.setName("Smartphone");
        product.setPrice(15000.0);
        product.setStockQuantity(20);

        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setUser(user);
    }

    @Test
    void testCreateOrderFromCart_Success() {
        when(cartItemRepository.findByUser(user)).thenReturn(Arrays.asList(cartItem));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order order = orderService.createOrderFromCart(user, "Vadgaon, Pune");

        assertNotNull(order);
        assertEquals(30000.0, order.getTotalPrice());
        assertEquals(18, product.getStockQuantity());
        verify(productRepository).save(product);
        verify(cartItemRepository).deleteByUser(user);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testCreateOrderFromCart_EmptyCart_ThrowsException() {
        when(cartItemRepository.findByUser(user)).thenReturn(Collections.emptyList());

        assertThrows(IllegalArgumentException.class, () -> 
            orderService.createOrderFromCart(user, "Pune")
        );
    }

    @Test
    void testCreateOrderFromCart_OutOfStock_ThrowsException() {
        cartItem.setQuantity(50);
        when(cartItemRepository.findByUser(user)).thenReturn(Arrays.asList(cartItem));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            orderService.createOrderFromCart(user, "Pune")
        );
        assertTrue(exception.getMessage().contains("Insufficient stock"));
    }

    @Test
    void testUpdatePaymentStatus_Success() {
        Order order = new Order();
        order.setId(1L);
        order.setPaymentStatus("PENDING");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order updatedOrder = orderService.updatePaymentStatus(1L, "PAID");

        assertEquals("PAID", updatedOrder.getPaymentStatus());
        verify(orderRepository).save(order);
    }
}