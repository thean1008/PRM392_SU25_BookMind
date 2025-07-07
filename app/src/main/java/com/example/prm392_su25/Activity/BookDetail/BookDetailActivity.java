package com.example.prm392_su25.Activity.BookDetail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392_su25.Activity.Cart.CartActivity;
import com.example.prm392_su25.Activity.Main.MainActivity;
import com.example.prm392_su25.Interface.RetrofitClient;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.prm392_su25.Interface.ApiService;
import com.example.prm392_su25.Model.Book.AddToCart;
import com.example.prm392_su25.Model.Book.Book;
import com.example.prm392_su25.Interface.ApiResponse;
import com.example.prm392_su25.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {

    private TextView txtName, txtBrief, txtPrice, txtFullDescription, txtTechSpec;
    private ImageView imgProduct;
    private ApiService productApi;
    private Button btnAddToCart, btnIncrease, btnDecrease;
    private EditText edtQuantity;
    private int currentQuantity = 1;
    private int productId;
    private ImageButton btnCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        productApi = RetrofitClient.getClient().create(ApiService.class);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);
        edtQuantity = findViewById(R.id.edtQuantity);
        btnCart =findViewById(R.id.iMCart);
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(BookDetailActivity.this, CartActivity.class);
            startActivity(intent);
        });
// Lấy productId
        productId = getIntent().getIntExtra("productId", -1);
        if (productId != -1) {
            getProductDetail(productId);
        }

// Tăng số lượng
        btnIncrease.setOnClickListener(v -> {
            currentQuantity++;
            edtQuantity.setText(String.valueOf(currentQuantity));
        });

// Giảm số lượng
        btnDecrease.setOnClickListener(v -> {
            if (currentQuantity > 1) {
                currentQuantity--;
                edtQuantity.setText(String.valueOf(currentQuantity));
            }
        });

// Thêm vào giỏ hàng
        btnAddToCart.setOnClickListener(v -> {
            try {
                int quantity = Integer.parseInt(edtQuantity.getText().toString());
                if (quantity > 0) {
                    addToCart(productId, quantity);
                } else {
                    Toast.makeText(this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        // Ánh xạ view
        txtName = findViewById(R.id.txtName);
        txtBrief = findViewById(R.id.txtBrief);
        txtPrice = findViewById(R.id.txtPrice);

        txtFullDescription = findViewById(R.id.txtFullDescription);
        txtTechSpec = findViewById(R.id.txtTechSpec);
        imgProduct = findViewById(R.id.imgProduct);



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
    private void addToCart(int productId, int quantity) {
        AddToCart request = new AddToCart(productId, quantity);
        Call<Void> call = productApi.addToCart(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BookDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BookDetailActivity.this, "Thêm giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                    Log.e("BookDetailActivity", "Add to cart failed: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(BookDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
