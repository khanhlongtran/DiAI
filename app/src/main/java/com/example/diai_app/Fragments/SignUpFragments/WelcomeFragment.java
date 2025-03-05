package com.example.diai_app.Fragments.SignUpFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.diai_app.Fragments.BaseFragment;
import com.example.diai_app.R;

public class WelcomeFragment extends BaseFragment {
    AppCompatButton btnSignupWithEmail;

    public WelcomeFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_welcome;
    }

    @Override
    protected void bindView(View view) {
        btnSignupWithEmail = view.findViewById(R.id.btnSignupWithEmail);
    }

    @Override
    protected void addOnEventListener() {
        btnSignupWithEmail.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SignUpFragment1()) // Chuyển sang SignupFragment1
                    .addToBackStack(null) // Cho phép quay lại WelcomeFragment nếu cần
                    .commit();
        });
    }
}
