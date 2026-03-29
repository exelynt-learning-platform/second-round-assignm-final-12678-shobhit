package com.exelynt.ecommerce.dto;

public class PaymentRequest {
    private Long amount;
    private String currency;
    private Long orderId; 

    public PaymentRequest() {}

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
}