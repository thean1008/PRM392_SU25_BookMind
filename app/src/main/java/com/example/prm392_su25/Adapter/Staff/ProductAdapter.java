package com.example.prm392_su25.Adapter.Staff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_su25.Model.Book.Book;
import com.example.prm392_su25.R;
import com.example.prm392_su25.ViewHolder.Staff.ProductViewHolder;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private final List<Book> productList;
    private final OnProductClickListener listener;

    public interface OnProductClickListener {
        void onEditClick(Book product);
        void onDeleteClick(Book product);
    }

    public ProductAdapter(List<Book> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_staff_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Book product = productList.get(position);

        holder.txtProductName.setText(product.getProductName());

        // Set Category Name
        String categoryName = product.getCategoryName();
        if (categoryName == null || categoryName.isEmpty()) {
            categoryName = "Không rõ";
        }
        holder.txtProductCategory.setText("Danh mục: " + categoryName);

        String priceText = holder.itemView.getContext().getString(
                R.string.product_price_format,
                product.getPrice()
        );
        holder.txtProductPrice.setText(priceText);

        Glide.with(holder.itemView.getContext())
                .load(product.getImageURL())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imgBook);

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(product));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
