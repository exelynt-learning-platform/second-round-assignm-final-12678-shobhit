package com.exelynt.ecommerce.service;

import com.exelynt.ecommerce.model.User;
import com.exelynt.ecommerce.model.Product;
import com.exelynt.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final UserRepository userRepository;

    public CartService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addToCart(User user, Product product) {
        user.getCartProducts().add(product); 
        userRepository.save(user);
    }
}