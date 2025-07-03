package com.example.prm392_su25.Model.Cart;

public class UpdateCartRequest {
    private int productID;
    private int quantity;

    public UpdateCartRequest(int productID, int quantity) {
        this.productID = productID;
        this.quantity = quantity;
    }

    public int getProductID() {
        return productID;
    }

    public int getQuantity() {
        return quantity;
    }
}
