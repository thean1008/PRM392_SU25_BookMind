package com.example.prm392_su25.Fragment.Profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.prm392_su25.Interface.ApiResponse;
import com.example.prm392_su25.Interface.ApiService;
import com.example.prm392_su25.Interface.RetrofitClient;
import com.example.prm392_su25.Model.Profile.Profile;
import com.example.prm392_su25.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private ImageView imgAvatar;
    private TextInputEditText edtFullName, edtUserName, edtEmail, edtAddress, edtDateOfBirth;
    private TextView txtRole;
    private MaterialButton btnEdit, btnSave;
    private ApiService apiService;
    private Profile profileData;
    private String imagePath;

    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        imgAvatar = fragmentView.findViewById(R.id.imgAvatar);
        edtFullName = fragmentView.findViewById(R.id.edtFullName);
        edtUserName = fragmentView.findViewById(R.id.edtUserName);
        edtEmail = fragmentView.findViewById(R.id.edtEmail);
        edtAddress = fragmentView.findViewById(R.id.edtAddress);
        edtDateOfBirth = fragmentView.findViewById(R.id.edtDateOfBirth);
        txtRole = fragmentView.findViewById(R.id.txtRole);
        btnEdit = fragmentView.findViewById(R.id.btnEdit);
        btnSave = fragmentView.findViewById(R.id.btnSave);

        // Click chọn ảnh
        imgAvatar.setOnClickListener(v -> {
            if (btnSave.getVisibility() == View.VISIBLE) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(intent);
            }
        });

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        imagePath = selectedImage != null ? selectedImage.toString() : null;

                        if (imagePath != null) {
                            Glide.with(requireContext())
                                    .load(selectedImage)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.error)
                                    .circleCrop()
                                    .into(imgAvatar);
                        }
                    }
                });

        btnEdit.setOnClickListener(v -> {
            enableEdit(true);
            btnEdit.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
        });

        btnSave.setOnClickListener(v -> saveProfile());

        loadProfile();

        return fragmentView;
    }

    private void loadProfile() {
        apiService.getProfile().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Profile>> call,
                                   @NonNull Response<ApiResponse<Profile>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess) {
                    profileData = response.body().getResult();
                    displayProfile(profileData);
                } else {
                    Toast.makeText(requireContext(), "Không lấy được dữ liệu.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Profile>> call,
                                  @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProfile(@NonNull Profile profile) {
        edtFullName.setText(nullSafe(profile.getFullName()));
        edtUserName.setText(nullSafe(profile.getUserName()));
        edtEmail.setText(nullSafe(profile.getEmail()));
        edtAddress.setText(nullSafe(profile.getAddress()));
        edtDateOfBirth.setText(
                profile.getDateOfBirth() != null
                        ? profile.getDateOfBirth().substring(0, Math.min(10, profile.getDateOfBirth().length()))
                        : ""
        );
        txtRole.setText(nullSafe(profile.getRole()));

        String imageUrl = profile.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .circleCrop()
                    .into(imgAvatar);
        } else {
            imgAvatar.setImageResource(R.drawable.placeholder);
        }

        enableEdit(false);
    }

    private void enableEdit(boolean enable) {
        edtFullName.setEnabled(enable);
        edtEmail.setEnabled(enable);
        edtAddress.setEnabled(enable);
        edtDateOfBirth.setEnabled(enable);
    }

    private void saveProfile() {
        if (profileData == null) return;

        profileData.setFullName(nullSafe(edtFullName.getText()));
        profileData.setEmail(nullSafe(edtEmail.getText()));
        profileData.setAddress(nullSafe(edtAddress.getText()));
        profileData.setDateOfBirth(nullSafe(edtDateOfBirth.getText()));

        if (imagePath != null) {
            profileData.setImageUrl(imagePath);
        }

        apiService.updateProfile(profileData).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess) {
                    Toast.makeText(requireContext(), response.body().result, Toast.LENGTH_SHORT).show();
                    enableEdit(false);
                    btnEdit.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.GONE);
                } else {
                    Toast.makeText(requireContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Helper để tránh NullPointerException khi setText hoặc toString()
     */
    private String nullSafe(CharSequence s) {
        return s != null ? s.toString() : "";
    }

    private String nullSafe(String s) {
        return s != null ? s : "";
    }
}
