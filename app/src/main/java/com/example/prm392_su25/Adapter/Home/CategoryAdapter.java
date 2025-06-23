package com.example.prm392_su25.Adapter.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_su25.R;
import com.example.prm392_su25.Model.Home.CategoryWithProducts;
import com.example.prm392_su25.ViewHolder.Home.CategoryViewHolder;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private Context context;
    private List<CategoryWithProducts> categoryList;

    public CategoryAdapter(Context context, List<CategoryWithProducts> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryWithProducts item = categoryList.get(position);
        holder.tvCategoryName.setText(item.getCategory().getCategoryName());

        // Gán RecyclerView ngang cho từng category
        ProductAdapter productAdapter = new ProductAdapter(context, item.getProducts());
        holder.rvProduct.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rvProduct.setAdapter(productAdapter);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}