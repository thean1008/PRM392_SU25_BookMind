package com.example.prm392_su25.Adapter.Book;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_su25.Activity.BookDetail.BookDetailActivity;
import com.example.prm392_su25.Model.Book.Book;
import com.example.prm392_su25.R;
import com.example.prm392_su25.ViewHolder.Book.BookViewHolder;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {

    private List<Book> books;

    public BookAdapter(List<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.name.setText(book.getProductName());
        holder.brief.setText(book.getBriefDescription());
        int price = book.getPrice();
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(price);

        holder.price.setText(formattedPrice + " VNĐ");

        Glide.with(holder.itemView.getContext())
                .load(book.getImageURL())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
            intent.putExtra("productId", book.getProductID());
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
