package com.exelynt.ecommerce.controller;

import com.exelynt.ecommerce.dto.PaymentRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    public PaymentController() {
        
        Stripe.apiKey = "sk_test_4eC39HqLyjWDarjtT1zdp7dc"; 
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentRequest paymentRequest) {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(paymentRequest.getAmount()) 
                    .setCurrency(paymentRequest.getCurrency())
                
                    .putMetadata("order_id", String.valueOf(paymentRequest.getOrderId()))
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", intent.getClientSecret());
            response.put("paymentIntentId", intent.getId());
            
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
           
            throw new RuntimeException("Stripe Error: " + e.getMessage());
        }
    }
}