package com.exelynt.ecommerce.controller;

import com.exelynt.ecommerce.model.User;
import com.exelynt.ecommerce.repository.UserRepository; // Import UserRepository
import com.exelynt.ecommerce.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private UserRepository userRepository; 
    @Test
    @WithMockUser(username = "shobhit@exelynt.com")
    void testAddToCart_Authenticated() throws Exception {
        
        User dummyUser = new User();
        dummyUser.setEmail("shobhit@exelynt.com");
        dummyUser.setId(1L);
        
       
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(dummyUser));

        mockMvc.perform(post("/api/cart/add/1")
                .param("quantity", "1"))
                .andExpect(status().isOk());
    }
}