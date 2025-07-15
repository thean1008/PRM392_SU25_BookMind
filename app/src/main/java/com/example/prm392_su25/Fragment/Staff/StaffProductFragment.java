package com.example.prm392_su25.Fragment.Staff;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_su25.Adapter.Staff.ProductAdapter;
import com.example.prm392_su25.Interface.ApiResponse;
import com.example.prm392_su25.Interface.ApiService;
import com.example.prm392_su25.Interface.RetrofitClient;
import com.example.prm392_su25.Model.Book.Book;
import com.example.prm392_su25.Model.Home.Category;
import com.example.prm392_su25.Network.CloudinaryUploader;
import com.example.prm392_su25.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffProductFragment extends Fragment {

    private RecyclerView recyclerView;
    private MaterialButton btnAddProduct;
    private TextInputEditText edtSearch;
    private ProductAdapter adapter;
    private List<Book> products;

    private Uri selectedImageUri = null;
    private String uploadedImageUrl = null;

    private List<Category> categoryList = new ArrayList<>();
    private Spinner spinnerCategory;
    private ImageView imgPreview;

    private final Map<Integer, String> categoryMap = new HashMap<>();
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_product, container, false);

        recyclerView = view.findViewById(R.id.rcvProduct);
        btnAddProduct = view.findViewById(R.id.btnAddProduct);
        edtSearch = view.findViewById(R.id.edtSearch);

        products = new ArrayList<>();
        adapter = new ProductAdapter(products, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onEditClick(Book product) {
                showAddOrEditDialog(product);
            }

            @Override
            public void onDeleteClick(Book product) {
                confirmDelete(product);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        btnAddProduct.setOnClickListener(v -> showAddOrEditDialog(null));

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            String keyword = edtSearch.getText().toString().trim();
            if (!keyword.isEmpty()) {
                searchProducts(keyword);
            } else {
                loadProducts();
            }
            return true;
        });

        loadCategories(null, null);

        return view;
    }

    private void loadCategories(@Nullable Spinner spinner, @Nullable Book editingBook) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<ApiResponse<List<Category>>> call = api.getAllCategories();

        call.enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Category>>> call,
                                   @NonNull Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body().getResult();
                    categoryMap.clear();
                    for (Category cat : categoryList) {
                        categoryMap.put(cat.getCategoryID(), cat.getCategoryName());
                    }

                    if (spinner != null) {
                        ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                categoryList
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        if (editingBook != null) {
                            for (int i = 0; i < categoryList.size(); i++) {
                                if (categoryList.get(i).getCategoryID() == editingBook.getCategoryID()) {
                                    spinner.setSelection(i);
                                    break;
                                }
                            }
                        }
                    }

                    loadProducts();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Category>>> call,
                                  @NonNull Throwable t) {
                showMessage("Lỗi tải danh sách category.");
            }
        });
    }

    private void loadProducts() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<ApiResponse<List<Book>>> call = api.getBooks();

        call.enqueue(new Callback<ApiResponse<List<Book>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Book>>> call,
                                   @NonNull Response<ApiResponse<List<Book>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    products.clear();
                    List<Book> result = response.body().getResult();
                    if (result != null) {
                        for (Book book : result) {
                            String categoryName = categoryMap.get(book.getCategoryID());
                            if (categoryName == null) {
                                categoryName = "Không rõ";
                            }
                            book.setCategoryName(categoryName);
                        }
                        products.addAll(result);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    showMessage("Tải sản phẩm thất bại.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Book>>> call,
                                  @NonNull Throwable t) {
                Log.e("API_ERROR", t.getMessage(), t);
                showMessage("Lỗi kết nối server.");
            }
        });
    }

    private void searchProducts(String keyword) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<ApiResponse<List<Book>>> call = api.searchProducts(keyword);

        call.enqueue(new Callback<ApiResponse<List<Book>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Book>>> call,
                                   @NonNull Response<ApiResponse<List<Book>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    products.clear();
                    List<Book> result = response.body().getResult();
                    if (result != null) {
                        for (Book book : result) {
                            String categoryName = categoryMap.get(book.getCategoryID());
                            if (categoryName == null) {
                                categoryName = "Không rõ";
                            }
                            book.setCategoryName(categoryName);
                        }
                        products.addAll(result);
                    }
                    adapter.notifyDataSetChanged();
                    showMessage("Tìm thấy " + products.size() + " sách.");
                } else {
                    showMessage("Không tìm thấy sản phẩm.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Book>>> call,
                                  @NonNull Throwable t) {
                Log.e("API_ERROR", t.getMessage(), t);
                showMessage("Lỗi kết nối server.");
            }
        });
    }

    private void showAddOrEditDialog(@Nullable Book editingBook) {
        selectedImageUri = null;
        uploadedImageUrl = null;

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View view = inflater.inflate(R.layout.dialog_add_product, null);

        TextInputEditText edtProductName = view.findViewById(R.id.edtProductName);
        TextInputEditText edtBriefDescription = view.findViewById(R.id.edtBriefDescription);
        TextInputEditText edtFullDescription = view.findViewById(R.id.edtFullDescription);
        TextInputEditText edtTechnicalSpecifications = view.findViewById(R.id.edtTechnicalSpecifications);
        TextInputEditText edtPrice = view.findViewById(R.id.edtPrice);
        imgPreview = view.findViewById(R.id.imgPreview);
        Button btnSelectImage = view.findViewById(R.id.btnSelectImage);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);

        loadCategories(spinnerCategory, editingBook);

        if (editingBook != null) {
            edtProductName.setText(editingBook.getProductName());
            edtBriefDescription.setText(editingBook.getBriefDescription());
            edtFullDescription.setText(editingBook.getFullDescription());
            edtTechnicalSpecifications.setText(editingBook.getTechnicalSpecifications());
            edtPrice.setText(String.valueOf(editingBook.getPrice()));

            if (editingBook.getImageURL() != null && !editingBook.getImageURL().isEmpty()) {
                Glide.with(requireContext())
                        .load(editingBook.getImageURL())
                        .into(imgPreview);
            }
        }

        btnSelectImage.setOnClickListener(v -> openGallery());

        new AlertDialog.Builder(requireContext())
                .setTitle(editingBook == null ? "Thêm sách" : "Sửa sách")
                .setView(view)
                .setPositiveButton(editingBook == null ? "Thêm" : "Cập nhật", (dialog, which) -> {
                    if (selectedImageUri != null && uploadedImageUrl == null) {
                        showMessage("Vui lòng chờ upload ảnh xong!");
                        return;
                    }

                    Book book = editingBook != null ? editingBook : new Book();

                    book.setProductName(edtProductName.getText().toString().trim());
                    book.setBriefDescription(edtBriefDescription.getText().toString().trim());
                    book.setFullDescription(edtFullDescription.getText().toString().trim());
                    book.setTechnicalSpecifications(edtTechnicalSpecifications.getText().toString().trim());

                    String priceStr = edtPrice.getText().toString().trim();
                    int price = 0;
                    if (!priceStr.isEmpty()) {
                        try {
                            price = Integer.parseInt(priceStr);
                        } catch (NumberFormatException e) {
                            price = 0;
                        }
                    }
                    book.setPrice(price);

                    if (uploadedImageUrl != null) {
                        book.setImageURL(uploadedImageUrl);
                    } else if (editingBook != null) {
                        book.setImageURL(editingBook.getImageURL());
                    } else {
                        book.setImageURL("");
                    }

                    Category selected = (Category) spinnerCategory.getSelectedItem();
                    if (selected != null) {
                        book.setCategoryID(selected.getCategoryID());
                        book.setCategoryName(selected.getCategoryName());
                    }

                    if (editingBook == null) {
                        addProduct(book);
                    } else {
                        book.setProductID(editingBook.getProductID());
                        updateProduct(book);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (imgPreview != null) {
                imgPreview.setImageURI(selectedImageUri);
            }

            showLoading("Đang upload ảnh...");
            CloudinaryUploader.uploadImage(requireContext(), selectedImageUri, new CloudinaryUploader.UploadCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    uploadedImageUrl = imageUrl;
                    requireActivity().runOnUiThread(() -> {
                        hideLoading();
                        showMessage("Upload Cloudinary thành công!");
                    });
                }

                @Override
                public void onFailure(String error) {
                    requireActivity().runOnUiThread(() -> {
                        hideLoading();
                        showMessage("Upload thất bại: " + error);
                    });
                }
            });
        }
    }

    private void showLoading(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void addProduct(Book book) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<ApiResponse<Book>> call = api.createProduct(book);

        call.enqueue(new Callback<ApiResponse<Book>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Book>> call,
                                   @NonNull Response<ApiResponse<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showMessage("Thêm sách thành công.");
                    loadProducts();
                } else {
                    showMessage("Thêm sách thất bại.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Book>> call,
                                  @NonNull Throwable t) {
                showMessage("Lỗi kết nối server.");
            }
        });
    }

    private void updateProduct(Book book) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<ApiResponse<Book>> call = api.updateProduct(book);

        call.enqueue(new Callback<ApiResponse<Book>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Book>> call,
                                   @NonNull Response<ApiResponse<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showMessage("Cập nhật sách thành công.");
                    loadProducts();
                } else {
                    showMessage("Cập nhật sách thất bại.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Book>> call,
                                  @NonNull Throwable t) {
                showMessage("Lỗi kết nối server.");
            }
        });
    }

    private void confirmDelete(Book book) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xoá")
                .setMessage("Bạn muốn xoá sách " + book.getProductName() + "?")
                .setPositiveButton("Xoá", (dialog, which) -> deleteProduct(book.getProductID()))
                .setNegativeButton("Huỷ", null)
                .show();
    }

    private void deleteProduct(int productId) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<ApiResponse<Void>> call = api.deleteProduct(productId);

        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call,
                                   @NonNull Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    showMessage("Xoá sách thành công.");
                    loadProducts();
                } else {
                    showMessage("Xoá sách thất bại.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call,
                                  @NonNull Throwable t) {
                showMessage("Lỗi kết nối server.");
            }
        });
    }

    private void showMessage(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }
}
