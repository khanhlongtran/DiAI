package com.example.diai_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.diai_app.Fragments.ActivityFragment;
import com.example.diai_app.Fragments.CaloriesFragment;
import com.example.diai_app.Fragments.ChatBotFragment;
import com.example.diai_app.Fragments.HomeFragment;
import com.example.diai_app.Fragments.ShopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);

        // Mặc định hiển thị HomeFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_calories) {
                selectedFragment = new CaloriesFragment();
            } else if (item.getItemId() == R.id.nav_bot) {
                selectedFragment = new ChatBotFragment();
            } else if (item.getItemId() == R.id.nav_activity) {
                selectedFragment = new ActivityFragment();
            } else if (item.getItemId() == R.id.nav_shop) {
                selectedFragment = new ShopFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}