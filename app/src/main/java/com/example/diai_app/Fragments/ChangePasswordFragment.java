package com.example.diai_app.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chaos.view.PinView;
import com.example.diai_app.DataModel.User;
import com.example.diai_app.Manager.UserManager;
import com.example.diai_app.R;

public class ChangePasswordFragment extends BaseFragment {
    private EditText etNewPassword, etConfirmPassword;
    private Button btnChangePassword;
    private static final String ARG_EMAIL = "email";
    private String email;

    public static ChangePasswordFragment newInstance(String email) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_EMAIL);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_change_password;
    }

    @Override
    protected void bindView(View view) {
        etNewPassword = view.findViewById(R.id.etNewPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
    }

    @Override
    protected void addOnEventListener() {
        btnChangePassword.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String newPassword = etNewPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        // Tìm User bằng email
        User user = UserManager.getInstance().findUserByEmail(email);
        if (user == null) {
            Toast.makeText(getContext(), "Người dùng không tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }
        // Cập nhật mật khẩu mới
        user.setPassword(newPassword);
        Toast.makeText(getContext(), "Mật khẩu đã được thay đổi!", Toast.LENGTH_SHORT).show();
    }
}
