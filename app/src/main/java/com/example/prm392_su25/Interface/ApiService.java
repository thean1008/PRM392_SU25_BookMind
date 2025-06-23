package com.example.prm392_su25.Interface;
import com.example.prm392_su25.ApiResponse;
import com.example.prm392_su25.Model.Home.Category;
import com.example.prm392_su25.Model.Home.Product;
import com.example.prm392_su25.Model.Login.LoginRequest;
import com.example.prm392_su25.Model.Login.LoginResponse;
import com.example.prm392_su25.Model.Register.Register;
import com.example.prm392_su25.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("Auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
    @POST("Auth/register")
    Call<Void> register(@Body Register request);
    @GET("Category")
    Call<ApiResponse<List<Category>>> getAllCategories();
    @GET("Product/category/{id}")
    Call<ApiResponse<List<Product>>> getProductsByCategory(@Path("id") int categoryId);

    static ApiService api = RetrofitClient.getClient().create(ApiService.class);
}

