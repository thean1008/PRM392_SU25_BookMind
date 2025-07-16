package com.example.prm392_su25.Adapter.PaymentHistory;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_su25.Model.Payment.PaymentItem;
import com.example.prm392_su25.R;

import java.util.List;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ItemViewHolder> {

    private List<PaymentItem> items;

    public ProductItemAdapter(List<PaymentItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_payment, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        PaymentItem item = items.get(position);
        holder.tvProductName.setText("Sản phẩm: " + item.productName);
        holder.tvQuantity.setText("Số lượng: " + item.quantity);
        Glide.with(holder.itemView.getContext()).load(item.imageUrl).into(holder.ivProduct);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvQuantity;
        ImageView ivProduct;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivProduct = itemView.findViewById(R.id.ivProduct);
        }
    }
}
