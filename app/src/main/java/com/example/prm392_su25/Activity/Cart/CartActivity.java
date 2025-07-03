package com.example.prm392_su25.Activity.Cart;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_su25.Adapter.Cart.CartAdapter;
import com.example.prm392_su25.Interface.ApiService;
import com.example.prm392_su25.Interface.RetrofitClient;
import com.example.prm392_su25.Interface.ApiResponse;
import com.example.prm392_su25.Model.Cart.CartItem;
import com.example.prm392_su25.Model.Cart.Cart;
import com.example.prm392_su25.Model.Cart.UpdateCartRequest;
import com.example.prm392_su25.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView txtTotalPrice;
    private Button btnClearCart;

    private CartAdapter cartAdapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerCart);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        btnClearCart = findViewById(R.id.btnClearCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiService = RetrofitClient.getClient().create(ApiService.class);

        loadCart();

        btnClearCart.setOnClickListener(view -> clearCart());
    }

    private void loadCart() {
        apiService.getCart().enqueue(new Callback<ApiResponse<Cart>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Cart cart = response.body().getResult();
                    List<CartItem> items = cart.getItems();
                    Log.d("CART", "Items count: " + items.size());
                    if (items.isEmpty()) {
                        txtTotalPrice.setText("Tổng: 0 VND");
                    } else {
                        txtTotalPrice.setText("Tổng: " + cart.getTotalPrice() + " VND");
                    }
                    txtTotalPrice.setText("Tổng: " + cart.getTotalPrice() + " VND");

                    cartAdapter = new CartAdapter(CartActivity.this,items, new CartAdapter.OnCartItemListener() {
                        @Override
                        public void onIncrease(CartItem item) {
                            updateQuantity(item.getProductID(), item.getQuantity() + 1);
                        }

                        @Override
                        public void onDecrease(CartItem item) {
                            if (item.getQuantity() > 1) {
                                updateQuantity(item.getProductID(), item.getQuantity() - 1);
                            }
                        }

                        @Override
                        public void onDelete(CartItem item) {
                            removeItem(item.getProductID());
                        }
                    });
                    recyclerView.setAdapter(cartAdapter);
                } else {
                    Toast.makeText(CartActivity.this, "Lỗi tải giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cart>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateQuantity(int productId, int newQuantity) {
        apiService.updateCart(new UpdateCartRequest(productId, newQuantity)).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CartActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    loadCart();
                } else {
                    Toast.makeText(CartActivity.this, "Cập nhật số lượng thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void removeItem(int productId) {
        apiService.removeItem(productId).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    // KHÔNG check response.body() nữa
                    Toast.makeText(CartActivity.this, "Xoá sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    loadCart(); // Load lại
                } else {
                    Toast.makeText(CartActivity.this, "Xoá sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                    Log.e("CART", "Remove failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                Log.e("CART", "Remove error: " + t.getMessage(), t);
            }
        });
    }


    private void clearCart() {
        apiService.clearCart().enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CartActivity.this, "Đã xoá giỏ hàng", Toast.LENGTH_SHORT).show();
                    loadCart();
                } else {
                    Toast.makeText(CartActivity.this, "Xoá giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

}