package com.example.prm392_su25.ViewHolder.Cart;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_su25.R;

public class CartItemViewHolder extends RecyclerView.ViewHolder {
    public TextView txtProductName, txtPrice, txtQuantity;
    public ImageView imgProduct;
    public Button btnIncrease, btnDecrease;
    public ImageButton btnDelete;

    public CartItemViewHolder(View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.txtProductName);
        txtPrice = itemView.findViewById(R.id.txtPrice);
        txtQuantity = itemView.findViewById(R.id.txtQuantity);
        imgProduct = itemView.findViewById(R.id.imgProduct);
        btnIncrease = itemView.findViewById(R.id.btnIncrease);
        btnDecrease = itemView.findViewById(R.id.btnDecrease);
        btnDelete = itemView.findViewById(R.id.btnDelete);
    }
}
