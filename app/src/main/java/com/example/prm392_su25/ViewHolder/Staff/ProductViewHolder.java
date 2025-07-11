package com.example.prm392_su25.ViewHolder.Staff;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_su25.R;
import com.google.android.material.button.MaterialButton;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    public ImageView imgBook;
    public TextView txtProductName;
    public TextView txtProductCategory;
    public TextView txtProductPrice;
    public MaterialButton btnEdit;
    public MaterialButton btnDelete;

    public ProductViewHolder(View itemView) {
        super(itemView);
        imgBook = itemView.findViewById(R.id.imgBook);
        txtProductName = itemView.findViewById(R.id.txtProductName);
        txtProductCategory = itemView.findViewById(R.id.txtProductCategory);
        txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
        btnEdit = itemView.findViewById(R.id.btnEditProduct);
        btnDelete = itemView.findViewById(R.id.btnDeleteProduct);
    }
}
