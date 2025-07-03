// CartAdapter.java
package com.example.prm392_su25.Adapter.Cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_su25.Interface.ApiResponse;
import com.example.prm392_su25.Model.Cart.Cart;
import com.example.prm392_su25.Model.Cart.CartItem;
import com.example.prm392_su25.R;
import com.example.prm392_su25.ViewHolder.Cart.CartItemViewHolder;

import java.util.List;

import retrofit2.Callback;

public class CartAdapter extends RecyclerView.Adapter<CartItemViewHolder> {

    private final Context context;
    private final List<CartItem> cartItems;
    private final OnCartItemListener listener;

    public interface OnCartItemListener {
        void onIncrease(CartItem item);
        void onDecrease(CartItem item);
        void onDelete(CartItem item);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }



    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.txtProductName.setText(item.getProductName());
        holder.txtPrice.setText(item.getPrice() + " VND");
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        Glide.with(context)
                .load(item.getImageURL())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.imgProduct);

        holder.btnIncrease.setOnClickListener(v -> listener.onIncrease(item));
        holder.btnDecrease.setOnClickListener(v -> listener.onDecrease(item));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
