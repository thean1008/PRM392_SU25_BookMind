package com.example.prm392_su25.Activity.Main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.prm392_su25.Activity.Cart.CartActivity;
import com.example.prm392_su25.Activity.Login.LoginActivity;
import com.example.prm392_su25.Activity.PaymentHistory.PaymentHistoryActivity;
import com.example.prm392_su25.Fragment.Book.BookFragment;
import com.example.prm392_su25.Fragment.Home.HomeFragment;
import com.example.prm392_su25.Fragment.Profile.ProfileFragment;
import com.example.prm392_su25.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageButton btnCart, btnMore;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        btnCart = findViewById(R.id.iMCart);
        btnMore = findViewById(R.id.iMMore);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Load Fragment mặc định
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, new HomeFragment())
                .commit();

        // Bấm nút giỏ hàng
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // Bấm nút More (3 gạch) → mở Drawer
        btnMore.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        // Load WebView map trong Drawer
        WebView webView = navigationView.findViewById(R.id.webViewMap);
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            String url = "https://www.openstreetmap.org/?mlat=10.839706&mlon=106.822276#map=18/10.839706/106.822276";
            webView.loadUrl(url);
        }

        // Xử lý nút Chỉ đường
        Button btnDirections = navigationView.findViewById(R.id.btnDirections);
        if (btnDirections != null) {
            btnDirections.setOnClickListener(v -> {
                String address = "FPT University HCM, Đường D1, Khu Công nghệ cao, Thủ Đức, Hồ Chí Minh";

                String url = "https://www.google.com/maps/dir/?api=1"
                        + "&destination=" + Uri.encode(address)
                        + "&travelmode=driving";

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            });
        }
        // Xử lý nút Lịch sử giao dịch
        Button btnTransactionHistory = navigationView.findViewById(R.id.btnTransactionHistory);
        if (btnTransactionHistory != null) {
            btnTransactionHistory.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, PaymentHistoryActivity.class);
                startActivity(intent);
            });
        }

// Xử lý nút Đăng xuất
        Button btnLogout = navigationView.findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {

                Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }



        // Xử lý Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_book) {
                selectedFragment = new BookFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
}
