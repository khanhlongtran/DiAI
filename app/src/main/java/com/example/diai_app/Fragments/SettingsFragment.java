package com.example.diai_app.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.diai_app.LoginActivity;
import com.example.diai_app.R;
import com.example.diai_app.Settings.SettingsUtils;

public class SettingsFragment extends BaseFragment {
    SharedPreferences sharedPreferences;
    Button logoutBtn;
    private RadioGroup themeRadioGroup;
    RadioButton rbLightMode, rbDarkMode;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        themeRadioGroup = view.findViewById(R.id.themeRadioGroup);
        rbLightMode = view.findViewById(R.id.rbLightMode);
        rbDarkMode = view.findViewById(R.id.rbDarkMode);


    }

    @Override
    protected void bindView(View view) {
        logoutBtn = view.findViewById(R.id.btnLogout2);
    }

    @Override
    protected void addOnEventListener() {
        logoutBtn.setOnClickListener(v -> logout());
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("loggedInUser");
        editor.remove("isLoggedIn");
        editor.apply();

        // Chuyển về LoginActivity
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
