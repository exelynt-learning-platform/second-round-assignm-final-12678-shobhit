package com.exelynt.ecommerce.service;

import com.exelynt.ecommerce.model.Product;
import com.exelynt.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testGetProductById_Success() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Techdrill 2K26 Badge");
      
        product.setPrice(new BigDecimal("199.00"));
        product.setStockQuantity(50);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Techdrill 2K26 Badge", result.getName());
        assertEquals(0, new BigDecimal("199.00").compareTo(result.getPrice()));
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            productService.getProductById(1L);
        });
    }
}