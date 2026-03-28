package com.exelynt.ecommerce.service;

import com.exelynt.ecommerce.model.Product;
import com.exelynt.ecommerce.model.User;
import com.exelynt.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void testAddToCart() {
        User user = new User();
        user.setCartProducts(new HashSet<>());
        Product product = new Product();
        product.setId(10L);

        cartService.addToCart(user, product);

        assertTrue(user.getCartProducts().contains(product));
        verify(userRepository, times(1)).save(user);
    }
}