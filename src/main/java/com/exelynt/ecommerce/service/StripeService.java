package com.exelynt.ecommerce.service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal; // Import zaroori hai

@Service
public class StripeService {

    @Value("${stripe.api.key:sk_test_4eC39HqLyjWDarjtT1zdp7dc}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    // Double ki jagah ab BigDecimal accept karega
    public String createCheckoutSession(BigDecimal amount) {
        try {
            // Stripe cents/paise mein amount leta hai (1 INR = 100 Paise)
            // Isliye amount ko 100 se multiply karke long mein convert kiya
            long unitAmount = amount.multiply(new BigDecimal(100)).longValue();

            SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8081/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:8081/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("inr")
                        .setUnitAmount(unitAmount) // Updated value
                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName("E-commerce Order Payment")
                            .build())
                        .build())
                    .build())
                .build();

            Session session = Session.create(params);
            return session.getUrl();
        } catch (Exception e) {
            throw new RuntimeException("Stripe Error: " + e.getMessage());
        }
    }
}