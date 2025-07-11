package com.example.prm392_su25.Fragment.Staff;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_su25.Adapter.Staff.CategoryAdapter;
import com.example.prm392_su25.Interface.ApiResponse;
import com.example.prm392_su25.Interface.ApiService;
import com.example.prm392_su25.Interface.RetrofitClient;
import com.example.prm392_su25.Model.Home.Category;
import com.example.prm392_su25.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffCategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<Category> categories;
    private TextInputEditText edtCategoryName;
    private MaterialButton btnAddCategory;

    private Category editingCategory = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_staff_category, container, false);

        recyclerView = view.findViewById(R.id.rcvCategory);
        edtCategoryName = view.findViewById(R.id.edtCategoryName);
        btnAddCategory = view.findViewById(R.id.btnAddCategory);

        categories = new ArrayList<>();
        adapter = new CategoryAdapter(categories, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onEditClick(Category category) {
                edtCategoryName.setText(category.getCategoryName());
                editingCategory = category;
                btnAddCategory.setText("Cập nhật");
            }

            @Override
            public void onDeleteClick(Category category) {
                confirmDelete(category);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        btnAddCategory.setOnClickListener(v -> {
            if (editingCategory == null) {
                addCategory();
            } else {
                updateCategory();
            }
        });

        loadCategories();

        return view;
    }

    /**
     * Load danh sách Category từ API
     */
    private void loadCategories() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<ApiResponse<List<Category>>> call = api.getAllCategories();

        call.enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Category>>> call,
                                   @NonNull Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories.clear();
                    List<Category> result = response.body().getResult();
                    if (result != null && !result.isEmpty()) {
                        categories.addAll(result);
                        adapter.notifyDataSetChanged();
                    } else {
                        showMessage("Danh sách trống.", false);
                    }
                } else {
                    showMessage("Tải danh sách thất bại.", false);
                }
                // Reset sau mỗi lần load
                editingCategory = null;
                btnAddCategory.setText("Thêm");
                edtCategoryName.setText("");
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Category>>> call,
                                  @NonNull Throwable t) {
                Log.e("API_ERROR", t.getMessage(), t);
                showMessage("Lỗi kết nối server.", false);
            }
        });
    }

    /**
     * Thêm mới Category
     */
    private void addCategory() {
        String name = edtCategoryName.getText() != null
                ? edtCategoryName.getText().toString().trim()
                : "";

        if (name.isEmpty()) {
            showMessage("Tên danh mục không được để trống.", false);
            return;
        }

        Category newCategory = new Category();
        newCategory.setCategoryName(name);

        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<ApiResponse<Category>> call = api.createCategory(newCategory);

        call.enqueue(new Callback<ApiResponse<Category>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Category>> call,
                                   @NonNull Response<ApiResponse<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showMessage("Thêm mới thành công.", true);
                    loadCategories();
                } else {
                    showMessage("Thêm mới thất bại.", false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Category>> call,
                                  @NonNull Throwable t) {
                Log.e("API_ERROR", t.getMessage(), t);
                showMessage("Lỗi kết nối server.", false);
            }
        });
    }

    /**
     * Cập nhật Category
     */
    private void updateCategory() {
        String name = edtCategoryName.getText() != null
                ? edtCategoryName.getText().toString().trim()
                : "";

        if (name.isEmpty()) {
            showMessage("Tên danh mục không được để trống.", false);
            return;
        }

        if (editingCategory == null) {
            showMessage("Không tìm thấy danh mục để cập nhật.", false);
            return;
        }

        Category updatedCategory = new Category();
        updatedCategory.setCategoryID(editingCategory.getCategoryID());
        updatedCategory.setCategoryName(name);

        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<ApiResponse<Category>> call = api.updateCategory(updatedCategory);

        call.enqueue(new Callback<ApiResponse<Category>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Category>> call,
                                   @NonNull Response<ApiResponse<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showMessage("Cập nhật thành công.", true);
                    loadCategories();
                } else {
                    showMessage("Cập nhật thất bại.", false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Category>> call,
                                  @NonNull Throwable t) {
                Log.e("API_ERROR", t.getMessage(), t);
                showMessage("Lỗi kết nối server.", false);
            }
        });
    }

    /**
     * Hiện dialog xác nhận xóa
     */
    private void confirmDelete(Category category) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa danh mục \"" + category.getCategoryName() + "\" không?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteCategory(category))
                .setNegativeButton("Hủy", null)
                .setCancelable(false)
                .show();
    }

    /**
     * Gọi API xóa Category
     */
    private void deleteCategory(Category category) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<ApiResponse<Void>> call = api.deleteCategory(category.getCategoryID());

        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call,
                                   @NonNull Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    showMessage("Xóa thành công.", true);
                    loadCategories();
                } else {
                    showMessage("Xóa thất bại.", false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call,
                                  @NonNull Throwable t) {
                Log.e("API_ERROR", t.getMessage(), t);
                showMessage("Lỗi kết nối server.", false);
            }
        });
    }

    /**
     * Hiển thị message bằng Snackbar
     */
    private void showMessage(String message, boolean success) {
        if (getView() != null) {
            Snackbar snackbar = Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT);
            int color = ContextCompat.getColor(requireContext(),
                    success ? android.R.color.holo_green_dark : android.R.color.holo_red_dark);
            snackbar.setBackgroundTint(color);
            snackbar.show();
        }
    }
}
