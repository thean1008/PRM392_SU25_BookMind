package com.example.prm392_su25.ViewHolder.Staff;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_su25.R;
import com.google.android.material.button.MaterialButton;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public TextView txtCategoryName;
    public MaterialButton btnEdit, btnDelete;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
        btnEdit = itemView.findViewById(R.id.btnEditCategory);
        btnDelete = itemView.findViewById(R.id.btnDeleteCategory);
    }
}
