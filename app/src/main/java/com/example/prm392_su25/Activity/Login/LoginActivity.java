package com.example.prm392_su25.Activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;


import com.example.prm392_su25.Activity.Main.MainActivity;
import com.example.prm392_su25.Activity.Register.RegisterActivity;
import com.example.prm392_su25.Interface.ApiService;
import com.example.prm392_su25.Model.Login.LoginRequest;
import com.example.prm392_su25.Model.Login.LoginResponse;
import com.example.prm392_su25.R;
import com.example.prm392_su25.Interface.RetrofitClient;
import com.example.prm392_su25.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText userNameEditText, passwordEditText;
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Liên kết View
        userNameEditText = findViewById(R.id.userName);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.loginButton2);

        // Sự kiện khi nhấn login
        loginButton.setOnClickListener(v -> performLogin());
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void performLogin() {
        String userName = userNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (userName.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(userName, password);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    String token = response.body().getResult().getToken();
                    TokenManager.saveToken(LoginActivity.this, token);
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công! " , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // để không quay lại login nữa
                } else {
                    Log.e("Login", "HTTP Code: " + response.code());

                    if (response.errorBody() != null) {
                        try {
                            Log.e("Login", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.body() != null && !response.body().isSuccess()) {
                        // Server trả 200 nhưng login thất bại
                        Toast.makeText(LoginActivity.this, "Sai thông tin đăng nhập.", Toast.LENGTH_SHORT).show();
                        Log.e("Login", "Login thất bại: isSuccess = false");
                    } else {
                        Toast.makeText(LoginActivity.this, "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                        Log.e("Login", "Không có errorBody, không có body");
                    }
                }
            }


            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Login", "onFailure: ", t);
            }
        });
    }
}
