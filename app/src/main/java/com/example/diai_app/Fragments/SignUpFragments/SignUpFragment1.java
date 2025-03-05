package com.example.diai_app.Fragments.SignUpFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.diai_app.Fragments.BaseFragment;
import com.example.diai_app.R;

public class SignUpFragment1 extends BaseFragment {
    private EditText etName, etEmail, etPassword;
    private AppCompatButton btnNext;
    private ImageView btnBack;

    public SignUpFragment1() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_signup1;
    }

    @Override
    protected void bindView(View view) {
        etName = view.findViewById(R.id.etUsernameRegister);
        etPassword = view.findViewById(R.id.etPasswordRegister);
        etEmail = view.findViewById(R.id.etEmailRegister);
        btnNext = view.findViewById(R.id.btnNext);
        btnBack = view.findViewById(R.id.btnBack1);
    }

    @Override
    protected void addOnEventListener() {
        // Xử lý sự kiện nhấn nút Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        btnNext.setOnClickListener(v -> {
            // Lấy thông tin từ EditText
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();

            // Tạo Fragment mới và truyền dữ liệu
            SignUpFragment2 fragment2 = new SignUpFragment2();
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("email", email);
            fragment2.setArguments(bundle);

            // Chuyển Fragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment2) // Thay thế bằng SignupFragment2
                    .addToBackStack(null) // Cho phép quay lại SignupFragment1 nếu cần
                    .commit();
        });

    }
}
