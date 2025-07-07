package com.example.prm392_su25.Activity.PaymentSuccess;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_su25.Activity.Main.MainActivity;
import com.example.prm392_su25.R;

public class PaymentSuccessActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        TextView txtMessage = findViewById(R.id.txtSuccessMessage);
        Button btnBackHome = findViewById(R.id.btnBackHome);

        txtMessage.setText("🎉 Thanh toán thành công!\nCảm ơn bạn đã mua hàng.");

        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentSuccessActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
