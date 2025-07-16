package com.example.prm392_su25.Network;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CloudinaryUploader {

    private static final String CLOUD_NAME = "dxqqozi6i";      // Thay bằng cloud name của em
    private static final String UPLOAD_PRESET = "bookmind";    // Thay bằng preset em đã tạo

    public interface UploadCallback {
        void onSuccess(String imageUrl);
        void onFailure(String error);
    }

    public static void uploadImage(Context context, Uri uri, UploadCallback callback) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            byte[] imageData = readBytes(inputStream);

            String url = "https://api.cloudinary.com/v1_1/" + CLOUD_NAME + "/image/upload";

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "image.jpg",
                            RequestBody.create(imageData, MediaType.parse("image/*")))
                    .addFormDataPart("upload_preset", UPLOAD_PRESET)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onFailure(e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        Log.d("Cloudinary", body);

                        // Lấy secure_url từ JSON
                        String secureUrl = extractSecureUrl(body);
                        callback.onSuccess(secureUrl);
                    } else {
                        callback.onFailure("Upload failed: " + response.code());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailure(e.getMessage());
        }
    }

    private static byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private static String extractSecureUrl(String json) {
        // Tìm secure_url trong JSON trả về
        int startIndex = json.indexOf("\"secure_url\":\"");
        if (startIndex == -1) return null;

        startIndex += "\"secure_url\":\"".length();
        int endIndex = json.indexOf("\"", startIndex);
        return json.substring(startIndex, endIndex).replace("\\/", "/");
    }
}
