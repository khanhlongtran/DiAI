package com.example.diai_app.Fragments.SignUpFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diai_app.R;

public class WelcomeFragment extends Fragment {

    public WelcomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        Button btnSignupWithEmail = view.findViewById(R.id.btnSignupWithEmail);
        btnSignupWithEmail.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SignUpFragment1()) // Chuyển sang SignupFragment1
                    .addToBackStack(null) // Cho phép quay lại WelcomeFragment nếu cần
                    .commit();
        });

        return view;
    }
}
