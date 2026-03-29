package com.exelynt.ecommerce.service;

import com.exelynt.ecommerce.model.User;
import com.exelynt.ecommerce.model.Product;
import com.exelynt.ecommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {
    private final UserRepository userRepository;

    public CartService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void addToCart(User user, Product product) {
        user.getCartProducts().add(product); 
        userRepository.save(user);
    }

    @Transactional
    public void removeFromCart(User user, Product product) {
        if (!user.getCartProducts().contains(product)) {
            throw new EntityNotFoundException("Product not found in user's cart");
        }
        user.getCartProducts().remove(product);
        userRepository.save(user);
    }

    public void validateCartOwnership(User user, Long authenticatedUserId) {
        if (!user.getId().equals(authenticatedUserId)) {
            throw new RuntimeException("Unauthorized: Access denied to this cart.");
        }
    }
}