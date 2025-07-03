package com.example.prm392_su25.Activity.Main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.prm392_su25.Activity.Cart.CartActivity;
import com.example.prm392_su25.Fragment.Book.BookFragment;
import com.example.prm392_su25.Fragment.Home.HomeFragment;
import com.example.prm392_su25.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageButton btnCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCart =findViewById(R.id.iMCart);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Default Fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, new HomeFragment())
                .commit();
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                }
            else if (item.getItemId() == R.id.nav_book) {
                  selectedFragment = new BookFragment();
            }
//             else if (item.getItemId() == R.id.nav_product) {
//                selectedFragment = new ProductFragment();
//            } else if (item.getItemId() == R.id.nav_cart) {
//                selectedFragment = new CartFragment();
//            }

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
