package com.example.prm392_su25.Activity.Checkout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_su25.R;

public class WebviewPaymentActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_payment);

        webView = findViewById(R.id.webViewPayment);

        // Nhận URL từ Intent
        String paymentUrl = getIntent().getStringExtra("payment_url");
        if (paymentUrl != null) {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true); // Cho phép JavaScript
            webSettings.setDomStorageEnabled(true);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    String url = request.getUrl().toString();

                    if (url.contains("vnpay-return") || url.contains("payment-success")) {
                        Intent intent = new Intent(WebviewPaymentActivity.this, com.example.prm392_su25.Activity.PaymentSuccess.PaymentSuccessActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        return true; // Ngăn WebView load URL
                    }

                    return false; // Cho phép WebView load URL
                }
            });

            webView.setWebChromeClient(new WebChromeClient());

            webView.loadUrl(paymentUrl);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
