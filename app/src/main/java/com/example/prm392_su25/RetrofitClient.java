package com.example.prm392_su25;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:5265/api/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();

                        // 🔐 Lấy token từ SharedPreferences thông qua context toàn cục
                        String token = TokenManager.getToken(MyApplication.getAppContext());

                        Request.Builder builder = original.newBuilder()
                                .method(original.method(), original.body());

                        if (token != null) {
                            builder.header("Authorization", "Bearer " + token);
                        }

                        Request request = builder.build();
                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

}
