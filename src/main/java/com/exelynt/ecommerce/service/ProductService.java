package com.exelynt.ecommerce.service;

import com.exelynt.ecommerce.model.Product;
import com.exelynt.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException; // Ye naya import hai
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() { 
        return productRepository.findAll(); 
    }
    
    public Product saveProduct(Product product) { 
        return productRepository.save(product); 
    }

    public Product getProductById(Long id) {
        // RuntimeException ki jagah EntityNotFoundException use kiya hai
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public void deleteProduct(Long id) { 
        productRepository.deleteById(id); 
    }
}