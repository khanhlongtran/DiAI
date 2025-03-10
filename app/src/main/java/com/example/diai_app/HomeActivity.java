package com.example.diai_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.diai_app.Fragments.ActivityFragment;
import com.example.diai_app.Fragments.CaloriesFragment;
import com.example.diai_app.Fragments.ChatBotFragment;
import com.example.diai_app.Fragments.HomeFragment;
import com.example.diai_app.Fragments.SettingsFragment;
import com.example.diai_app.Fragments.ShopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {
    FloatingActionButton fabChatBot;
    BottomNavigationView bottomNavigation;
    private FragmentManager fm;
    private Fragment activeFragment;
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment caloriesFragment = new CaloriesFragment();
    private final Fragment chatBotFragment = new ChatBotFragment();
    private final Fragment activityFragment = new ActivityFragment();
    private final Fragment shopFragment = new ShopFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        fabChatBot = findViewById(R.id.fabChatBot);
        // Đặt FAB ngoài cùng bên phải
        fabChatBot.post(() -> {
            View parentView = (View) fabChatBot.getParent();
            if (parentView != null) {
                fabChatBot.setX(parentView.getWidth() - fabChatBot.getWidth() - 50); // Cách phải 50px
                fabChatBot.setY(parentView.getHeight() - fabChatBot.getHeight() - 250); // Cách đáy 200px
            }
        });

        fm = getSupportFragmentManager();
        // Thêm Fragment vào container nhưng chỉ hiện HomeFragment
        fm.beginTransaction().add(R.id.fragment_container, shopFragment, "5").hide(shopFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, activityFragment, "4").hide(activityFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, chatBotFragment, "3").hide(chatBotFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, caloriesFragment, "2").hide(caloriesFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, homeFragment, "1").commit();

        activeFragment = homeFragment;

        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = homeFragment;
            } else if (item.getItemId() == R.id.nav_calories) {
                selectedFragment = caloriesFragment;
            } else if (item.getItemId() == R.id.nav_bot) {
                selectedFragment = chatBotFragment;
            } else if (item.getItemId() == R.id.nav_activity) {
                selectedFragment = activityFragment;
            } else if (item.getItemId() == R.id.nav_shop) {
                selectedFragment = shopFragment;
            }

            if (selectedFragment != null && selectedFragment != activeFragment) {
                fm.beginTransaction().hide(activeFragment).show(selectedFragment).commit();
                activeFragment = selectedFragment;
            }
            return true;
        });
//        // Mặc định hiển thị HomeFragment
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, new HomeFragment())
//                .commit();
//
//        bottomNavigation.setOnItemSelectedListener(item -> {
//            Fragment selectedFragment = null;
//
//            if (item.getItemId() == R.id.nav_home) {
//                selectedFragment = new HomeFragment();
//            } else if (item.getItemId() == R.id.nav_calories) {
//                selectedFragment = new CaloriesFragment();
//            } else if (item.getItemId() == R.id.nav_bot) {
//                selectedFragment = new ChatBotFragment();
//            } else if (item.getItemId() == R.id.nav_activity) {
//                selectedFragment = new ActivityFragment();
//            } else if (item.getItemId() == R.id.nav_shop) {
//                selectedFragment = new ShopFragment();
//            }
//
//            if (selectedFragment != null) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, selectedFragment)
//                        .commit();
//            }
//            return true;
//        });
        // Gán sự kiện kéo thả cho FAB
        enableDraggableFAB();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void enableDraggableFAB() {
        fabChatBot.setOnTouchListener(new View.OnTouchListener() {
            private float dX, dY;
            private int lastAction;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        v.setX(event.getRawX() + dX);
                        v.setY(event.getRawY() + dY);
                        lastAction = MotionEvent.ACTION_MOVE;
                        return true;

                    case MotionEvent.ACTION_UP:
                        // Nếu không có kéo, mở ChatBotFragment
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            openChatBot();
                        }
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void openChatBot() {
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.nav_bot); // Chuyển tab sang ChatBot
        }
    }
}