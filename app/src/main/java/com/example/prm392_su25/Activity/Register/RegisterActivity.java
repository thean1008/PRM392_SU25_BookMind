package com.example.prm392_su25.Activity.Register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_su25.Activity.Login.LoginActivity;
import com.example.prm392_su25.Interface.ApiService;
import com.example.prm392_su25.Model.Register.Register;
import com.example.prm392_su25.R;
import com.example.prm392_su25.Interface.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    ImageButton back;
    EditText userName, phone, email, address, password;
    Button btnRegister;
    CheckBox term;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userName = findViewById(R.id.rUserName);
        phone = findViewById(R.id.rPhone);
        email = findViewById(R.id.rEmail);
        address = findViewById(R.id.rAddress);
        password = findViewById(R.id.rPassword);
        btnRegister = findViewById(R.id.registerButton);
        term = findViewById(R.id.termAndPolicy);

        btnRegister.setOnClickListener(v ->{
            String username = userName.getText().toString().trim();
            String phoneNumber = phone.getText().toString().trim();
            String emailR = email.getText().toString().trim();
            String addressR = address.getText().toString().trim();
            String passwordR = password.getText().toString().trim();
            boolean isAgreed = term.isChecked();

            if(username.isEmpty() || phoneNumber.isEmpty() || emailR.isEmpty() || addressR.isEmpty() || passwordR.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!isAgreed){
                Toast.makeText(this, "Bạn phải đồng ý với điều khoản", Toast.LENGTH_SHORT).show();
                 return;
            }
            registerUser(username,phoneNumber,emailR,addressR,passwordR);
        });

        back = findViewById(R.id.imageView2);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void registerUser(String username, String phoneNumber, String email, String address, String password){
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        String fullName = username;
        String dateOfBirth = "2000-06-20T05:42:26.852Z";
        String imageUrl = "string";
        String role = "customer";
        Register request = new Register(fullName, username, phoneNumber, email,
                address, dateOfBirth, imageUrl, role, password);

        Call<Void> call = apiService.register(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công, vui lòng xác nhận email.", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        JSONArray errorMessages = jsonObject.optJSONArray("errorMessages");

                        if (errorMessages != null && errorMessages.length() > 0) {
                            StringBuilder errorBuilder = new StringBuilder();
                            for (int i = 0; i < errorMessages.length(); i++) {
                                errorBuilder.append("- ").append(errorMessages.getString(i)).append("\n");
                            }
                            Toast.makeText(RegisterActivity.this, errorBuilder.toString().trim(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}