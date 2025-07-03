package com.example.prm392_su25.Fragment.Book;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_su25.Adapter.Book.BookAdapter;
import com.example.prm392_su25.Interface.ApiResponse;
import com.example.prm392_su25.Interface.ApiService;
import com.example.prm392_su25.Model.Book.Book;
import com.example.prm392_su25.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private List<Book> bookList = new ArrayList<>();

    public BookFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        recyclerView = view.findViewById(R.id.recyclerBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BookAdapter(bookList);
        recyclerView.setAdapter(adapter);

        fetchBooks();

        return view;
    }

    private void fetchBooks() {
        ApiService.api.getBooks().enqueue(new Callback<ApiResponse<List<Book>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Book>>> call, Response<ApiResponse<List<Book>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess) {
                    List<Book> books = response.body().result;
                    bookList.clear();
                    bookList.addAll(books);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("BookFragment", "Error: " + (response.body() != null ? response.body().errorMessages : "Unknown error"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Book>>> call, Throwable t) {
                Log.e("BookFragment", "API call failed", t);
            }
        });
    }

}
