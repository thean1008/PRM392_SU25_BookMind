package com.example.prm392_su25.Activity.PaymentHistory;



import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_su25.Adapter.PaymentHistory.PaymentHistoryAdapter;
import com.example.prm392_su25.Interface.ApiResponse;
import com.example.prm392_su25.Interface.ApiService;
import com.example.prm392_su25.Interface.RetrofitClient;
import com.example.prm392_su25.Model.Payment.PaymentHistory;
import com.example.prm392_su25.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentHistoryActivity extends AppCompatActivity {

    private RecyclerView rvPaymentHistory;
    private PaymentHistoryAdapter adapter;
    private List<PaymentHistory> paymentList = new ArrayList<>();
    private ApiService paymentApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        rvPaymentHistory = findViewById(R.id.rvPaymentHistory);
        rvPaymentHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PaymentHistoryAdapter(paymentList);
        rvPaymentHistory.setAdapter(adapter);

        paymentApi = RetrofitClient.getClient().create(ApiService.class);
        loadPaymentHistory();
    }

    private void loadPaymentHistory() {
        paymentApi.getPaymentHistory().enqueue(new Callback<ApiResponse<List<PaymentHistory>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<PaymentHistory>>> call, Response<ApiResponse<List<PaymentHistory>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess) {
                    paymentList.clear();
                    paymentList.addAll(response.body().getResult());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PaymentHistoryActivity.this, "Không thể tải lịch sử thanh toán", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<PaymentHistory>>> call, Throwable t) {
                Toast.makeText(PaymentHistoryActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
