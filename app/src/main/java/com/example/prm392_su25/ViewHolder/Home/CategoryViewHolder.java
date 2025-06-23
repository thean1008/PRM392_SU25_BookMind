package com.example.prm392_su25.ViewHolder.Home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_su25.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public TextView tvCategoryName;
    public RecyclerView rvProduct;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        rvProduct = itemView.findViewById(R.id.rvProduct);
    }
}
