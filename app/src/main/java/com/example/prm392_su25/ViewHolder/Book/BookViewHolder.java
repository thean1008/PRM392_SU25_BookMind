package com.example.prm392_su25.ViewHolder.Book;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_su25.R;

public class BookViewHolder extends RecyclerView.ViewHolder {
    public TextView name, price, brief;
    public ImageView image;

    public BookViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.textViewTitle);
        brief = itemView.findViewById(R.id.textViewBrief);
        price = itemView.findViewById(R.id.textViewPrice);
        image = itemView.findViewById(R.id.imageViewCover);
    }
}
