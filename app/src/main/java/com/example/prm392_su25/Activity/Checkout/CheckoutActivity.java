package com.example.prm392_su25.Activity.Checkout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_su25.Interface.ApiResponse;
import com.example.prm392_su25.Interface.ApiService;
import com.example.prm392_su25.Model.Order.OrderRequest;
import com.example.prm392_su25.Model.Order.OrderResult;
import com.example.prm392_su25.Model.Payment.PaymentRequest;
import com.example.prm392_su25.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private EditText edtBillingAddress;
    private Button btnConfirmOrder;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        edtBillingAddress = findViewById(R.id.edtBillingAddress);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnConfirmOrder.setOnClickListener(v -> {
            String address = edtBillingAddress.getText().toString().trim();
            if (address.isEmpty()) {
                edtBillingAddress.setError("Vui lòng nhập địa chỉ");
                return;
            }

            createOrder(address);
        });
    }

    private void createOrder(String address) {
        OrderRequest orderRequest = new OrderRequest(address);

        ApiService.api.createOrder(orderRequest).enqueue(new Callback<ApiResponse<OrderResult>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrderResult>> call, Response<ApiResponse<OrderResult>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess) {
                    int orderId = response.body().getResult().getOrderID();
                    processPayment(orderId);
                } else {
                    Toast.makeText(CheckoutActivity.this, "Đặt hàng thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderResult>> call, Throwable t) {
                Toast.makeText(CheckoutActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processPayment(int orderId) {
        PaymentRequest request = new PaymentRequest(orderId);

        ApiService.api.processPayment(request).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess) {
                    String paymentUrl = response.body().getResult();
                    Intent intent = new Intent(CheckoutActivity.this, WebviewPaymentActivity.class);
                    intent.putExtra("payment_url", paymentUrl);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(CheckoutActivity.this, "Thanh toán thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(CheckoutActivity.this, "Lỗi khi thanh toán!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
