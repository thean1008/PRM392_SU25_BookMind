package com.example.prm392_su25.Fragment.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_su25.R;
import com.example.prm392_su25.Interface.RetrofitClient;
import com.example.prm392_su25.Adapter.Home.CategoryAdapter;
import com.example.prm392_su25.Interface.ApiService;
import com.example.prm392_su25.Interface.ApiResponse;
import com.example.prm392_su25.Model.Home.Category;
import com.example.prm392_su25.Model.Home.CategoryWithProducts;
import com.example.prm392_su25.Model.Home.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<CategoryWithProducts> categoryWithProductsList = new ArrayList<>();
    private ApiService apiService;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);  // dùng layout fragment_home.xml bạn đã tách

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        apiService = RetrofitClient.getClient().create(ApiService.class);

        fetchCategories();

        return view;
    }

    private void fetchCategories() {
        apiService.getAllCategories().enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess) {
                    List<Category> categories = response.body().result;
                    for (Category category : categories) {
                        fetchProductsByCategory(category);
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tải danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable t) {
                Toast.makeText(getContext(), "Không kết nối được server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchProductsByCategory(Category category) {
        apiService.getProductsByCategory(category.getCategoryID()).enqueue(new Callback<ApiResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Product>>> call, Response<ApiResponse<List<Product>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess) {
                    List<Product> products = response.body().result;

                    CategoryWithProducts combined = new CategoryWithProducts(category, products);
                    categoryWithProductsList.add(combined);

                    updateRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Product>>> call, Throwable t) {
                Log.e("HomeFragment", "Lỗi khi tải sản phẩm của category " + category.getCategoryID(), t);
            }
        });
    }

    private void updateRecyclerView() {
        if (categoryAdapter == null) {
            categoryAdapter = new CategoryAdapter(getContext(), categoryWithProductsList);
            recyclerView.setAdapter(categoryAdapter);
        } else {
            categoryAdapter.notifyDataSetChanged();
        }
    }
}
