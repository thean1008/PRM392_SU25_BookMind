package com.example.prm392_su25.Model.Payment;

import java.util.List;

public class PaymentHistory {
    public int paymentId;
    public int orderId;
    public int amount;
    public String paymentDate;
    public String paymentStatus;
    public List<PaymentItem> items;
}