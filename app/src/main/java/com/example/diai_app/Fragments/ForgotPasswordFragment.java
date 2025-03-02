package com.example.diai_app.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chaos.view.PinView;
import com.example.diai_app.R;

public class ForgotPasswordFragment extends BaseFragment {

    private EditText etEmail;
    private PinView pinView;
    private Button btnSendCode, btnVerifyOTP;
    private String generatedOTP;
    String email;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_forgot_password; // Đảm bảo file XML đúng
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        addOnEventListener();
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        etEmail = view.findViewById(R.id.etEmail);
        pinView = view.findViewById(R.id.pinview);
        btnSendCode = view.findViewById(R.id.btnSendCode);
        btnVerifyOTP = view.findViewById(R.id.btnVerifyOTP);
    }

    @Override
    protected void addOnEventListener() {
        super.addOnEventListener();

        btnSendCode.setOnClickListener(v -> sendOTP());

        btnVerifyOTP.setOnClickListener(v -> verifyOTP());
    }

    private void sendOTP() {
        email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kích hoạt nút xác minh OTP
        btnVerifyOTP.setEnabled(true);
    }

    private void verifyOTP() {
        String enteredOTP = pinView.getText().toString();
        if (enteredOTP.equals("000000")) {
            Toast.makeText(getContext(), "Xác minh thành công!", Toast.LENGTH_SHORT).show();
            // Chuyển sang ChangePasswordFragment và truyền email
            ChangePasswordFragment changePasswordFragment = ChangePasswordFragment.newInstance(email);

            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, changePasswordFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Toast.makeText(getContext(), "Mã OTP không chính xác!", Toast.LENGTH_SHORT).show();
        }
    }
}
