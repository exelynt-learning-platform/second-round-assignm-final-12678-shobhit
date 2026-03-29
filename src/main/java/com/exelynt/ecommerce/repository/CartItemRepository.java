package com.exelynt.ecommerce.repository;

import com.exelynt.ecommerce.model.CartItem;
import com.exelynt.ecommerce.model.Product; // Ye import zaroori hai
import com.exelynt.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
   
    
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    void deleteByUser(User user);
}