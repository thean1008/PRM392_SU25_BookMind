package com.example.prm392_su25.Activity.BookDetail;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.prm392_su25.Interface.ApiService;
import com.example.prm392_su25.Model.Book.Book;
import com.example.prm392_su25.ApiResponse;
import com.example.prm392_su25.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {

    private TextView txtName, txtBrief, txtPrice, txtFullDescription, txtTechSpec;
    private ImageView imgProduct;
    private ApiService productApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Ánh xạ view
        txtName = findViewById(R.id.txtName);
        txtBrief = findViewById(R.id.txtBrief);
        txtPrice = findViewById(R.id.txtPrice);
        txtFullDescription = findViewById(R.id.txtFullDescription);
        txtTechSpec = findViewById(R.id.txtTechSpec);
        imgProduct = findViewById(R.id.imgProduct);

        // Sử dụng ApiService đã có sẵn qua RetrofitClient
        productApi = ApiService.api;

        // Lấy productId từ Intent
        int productId = getIntent().getIntExtra("productId", -1);
        if (productId != -1) {
            getProductDetail(productId);
        }
    }

    private void getProductDetail(int id) {
        Call<ApiResponse<Book>> call = productApi.getProductById(id);
        call.enqueue(new Callback<ApiResponse<Book>>() {
            @Override
            public void onResponse(Call<ApiResponse<Book>> call, Response<ApiResponse<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Book book = response.body().getResult();

                    txtName.setText(book.getProductName());
                    txtBrief.setText(book.getBriefDescription());
                    txtPrice.setText(book.getPrice() + " VND");
                    txtFullDescription.setText(book.getFullDescription());
                    txtTechSpec.setText("Thông số kỹ thuật: " + book.getTechnicalSpecifications());

                    Glide.with(BookDetailActivity.this)
                            .load(book.getImageURL())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.error)
                            .into(imgProduct);
                } else {
                    Toast.makeText(BookDetailActivity.this, "Không thể lấy dữ liệu sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Book>> call, Throwable t) {
                Toast.makeText(BookDetailActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
