package com.example.prm392_su25.Model.Order;

public class OrderRequest {
    private String paymentMethod;
    private String billingAddress;
    private Integer storeLocationID; // có thể null

    public OrderRequest(String billingAddress) {
        this.paymentMethod = "VNPAY";
        this.billingAddress = billingAddress;
        this.storeLocationID = null;
    }

    // Getters and Setters
    public String getPaymentMethod() { return paymentMethod; }
    public String getBillingAddress() { return billingAddress; }
    public Integer getStoreLocationID() { return storeLocationID; }

    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
    public void setStoreLocationID(Integer storeLocationID) { this.storeLocationID = storeLocationID; }
}
