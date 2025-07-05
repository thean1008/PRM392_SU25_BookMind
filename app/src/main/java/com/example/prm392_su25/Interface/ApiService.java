package com.example.prm392_su25.Interface;
import com.example.prm392_su25.Model.Book.AddToCart;
import com.example.prm392_su25.Model.Book.Book;
import com.example.prm392_su25.Model.Cart.Cart;
import com.example.prm392_su25.Model.Cart.UpdateCartRequest;
import com.example.prm392_su25.Model.Home.Category;
import com.example.prm392_su25.Model.Home.Product;
import com.example.prm392_su25.Model.Login.LoginRequest;
import com.example.prm392_su25.Model.Login.LoginResponse;
import com.example.prm392_su25.Model.Profile.Profile;
import com.example.prm392_su25.Model.Register.Register;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    @GET("Product")
    Call<ApiResponse<List<Book>>> getBooks();
    @GET("Product/{id}")
    Call<ApiResponse<Book>> getProductById(@Path("id") int id);
    @POST("Cart/add")
    Call<Void> addToCart(@Body AddToCart cartRequest);
    @GET("Cart")
    Call<ApiResponse<Cart>> getCart();
    @PUT("Cart/update")
    Call<ApiResponse<String>> updateCart(@Body UpdateCartRequest request);
    @DELETE("Cart/remove/{productId}")
    Call<ApiResponse<String>> removeItem(@Path("productId") int productId);
    @DELETE("Cart/clear")
    Call<ApiResponse<String>> clearCart();

    @GET("Auth/profile")
    Call<ApiResponse<Profile>> getProfile();

    @PUT("Auth/profile")
    Call<ApiResponse<String>> updateProfile(@Body Profile profile);

    static ApiService api = RetrofitClient.getClient().create(ApiService.class);

}

