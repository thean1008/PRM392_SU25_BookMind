package com.example.prm392_su25.Adapter.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_su25.Model.Home.CategoryWithProducts;
import com.example.prm392_su25.Model.Home.Product;
import com.example.prm392_su25.R;
import com.example.prm392_su25.ViewHolder.Home.ProductViewHolder;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductName.setText(product.getProductName());
        int price = product.getPrice();
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(price);

        holder.tvPrice.setText(formattedPrice + " VNĐ");
        Glide.with(context)
                .load(product.getImageURL())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.ivProductImage);
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }
}
