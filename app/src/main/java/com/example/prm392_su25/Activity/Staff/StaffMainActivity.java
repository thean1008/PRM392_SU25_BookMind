package com.example.prm392_su25.Activity.Staff;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.prm392_su25.Fragment.Staff.StaffCategoryFragment;
import com.example.prm392_su25.Fragment.Staff.StaffProductFragment;
import com.example.prm392_su25.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StaffMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);

        bottomNavigationView = findViewById(R.id.staff_bottom_navigation);

        // Default fragment
        replaceFragment(new StaffCategoryFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            if (item.getItemId() == R.id.nav_staff_category) {
                fragment = new StaffCategoryFragment();
            } else if (item.getItemId() == R.id.nav_staff_product) {
                fragment = new StaffProductFragment();
            }

            if (fragment != null) {
                replaceFragment(fragment);
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.staff_nav_host_fragment, fragment)
                .commit();
    }
}
