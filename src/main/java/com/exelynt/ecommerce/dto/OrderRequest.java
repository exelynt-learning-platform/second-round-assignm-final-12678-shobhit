package com.exelynt.ecommerce.dto;

import java.util.List;

public class OrderRequest {
    private Long userId;
    private String shippingAddress;

    public OrderRequest() {}
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}