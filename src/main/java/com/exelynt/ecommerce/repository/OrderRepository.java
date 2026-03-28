package com.exelynt.ecommerce.repository;

import com.exelynt.ecommerce.model.Order;
import com.exelynt.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}