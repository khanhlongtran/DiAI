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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diai_app.DataModel.User;
import com.example.diai_app.HomeActivity;
import com.example.diai_app.Manager.UserManager;
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
                String password = bundle.getString("password", "");

                // Dữ liệu từ Fragment 2
                String fullName = bundle.getString("fullName", "");

                // Chuyển đổi thành số
                int age = 0;
                double height = 0;
                double weight = 0.0;
                try {
                    age = Integer.parseInt(bundle.getString("age", "0"));
                    height = Double.parseDouble(bundle.getString("height", "0"));
                    weight = Double.parseDouble(bundle.getString("weight", "0.0"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Invalid number format.", Toast.LENGTH_SHORT).show();
                    return; // Ngừng tiến trình đăng ký nếu có lỗi chuyển đổi
                }

                String sex = bundle.getString("sex", "");

                // Tạo User mới với các thông tin vừa lấy
                User newUser = new User(
                        name,
                        password,
                        email,
                        sex,
                        weight,
                        fullName,
                        age,
                        height,
                        diabetesType,
                        additionInfo,
                        hasFamilyHistory
                );
                // Thêm người dùng mới vào UserManager
                UserManager.getInstance().addUser(newUser);
                // Chuyển sang HomeActivity sau khi đăng ký thành công
                Intent intent = new Intent(requireActivity(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });


        return view;

    }
}
