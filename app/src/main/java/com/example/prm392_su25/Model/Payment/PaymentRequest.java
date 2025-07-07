package com.example.prm392_su25.Model.Payment;

public class PaymentRequest {
    private int orderID;
    private String paymentMethod;

    public PaymentRequest(int orderID) {
        this.orderID = orderID;
        this.paymentMethod = "VNPAY";
    }

    // Getters và Setters
    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
