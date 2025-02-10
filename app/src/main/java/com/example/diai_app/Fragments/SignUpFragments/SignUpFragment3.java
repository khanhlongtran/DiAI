package com.example.diai_app.Fragments.SignUpFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diai_app.HomeActivity;
import com.example.diai_app.R;

public class SignUpFragment3 extends Fragment {
    private Spinner spinnerDiabetesType, spinnerAdditionInfo;
    private CheckBox checkFamilyHistory;
    private Button btnStart;
    private ImageView btnBack;
    public SignUpFragment3() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup3, container, false);

        spinnerDiabetesType = view.findViewById(R.id.spinnerDiabetesType);
        spinnerAdditionInfo = view.findViewById(R.id.spinnerAdditionInfo);
        checkFamilyHistory = view.findViewById(R.id.checkFamilyHistory);
        btnStart = view.findViewById(R.id.btnStart);
        btnBack = view.findViewById(R.id.btnBack3);
        // Xử lý nút Back
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        // Gán danh sách loại tiểu đường vào Spinner
        ArrayAdapter<CharSequence> diabetesAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.diabetes_types, android.R.layout.simple_spinner_item);
        diabetesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiabetesType.setAdapter(diabetesAdapter);

        // Gán danh sách mục tiêu (Additional Info) vào Spinner
        ArrayAdapter<CharSequence> additionInfoAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.goal_array, android.R.layout.simple_spinner_item);
        additionInfoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdditionInfo.setAdapter(additionInfoAdapter);

        btnStart.setOnClickListener(v -> {
            String diabetesType = spinnerDiabetesType.getSelectedItem().toString();
            boolean hasFamilyHistory = checkFamilyHistory.isChecked();
            String additionInfo = spinnerAdditionInfo.getSelectedItem().toString();
            Bundle bundle = getArguments(); // Lấy toàn bộ dữ liệu từ Fragment trước đó
            if (bundle != null) {
                // Dữ liệu từ Fragment 1
                String name = bundle.getString("name", "");
                String email = bundle.getString("email", "");
                String password = bundle.getString("password", ""); // Nếu có password
                // Create User trước
                // Dữ liệu từ Fragment 2
                String fullName = bundle.getString("fullName", "");
                String age = bundle.getString("age", "");
                String height = bundle.getString("height", "");
                String sex = bundle.getString("sex", "");
                String weight = bundle.getString("weight", "");
                // Create Profile của User đo

                // Chuyển tất cả dữ liệu sang HomeActivity
                Intent intent = new Intent(requireActivity(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;

    }
}
