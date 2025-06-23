package com.example.prm392_su25.ViewHolder.Home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_su25.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivProductImage;
    public TextView tvProductName;
    public TextView tvPrice;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        ivProductImage = itemView.findViewById(R.id.ivProductImage);
        tvProductName = itemView.findViewById(R.id.tvProductName);
        tvPrice = itemView.findViewById(R.id.tvPrice);
    }
}
