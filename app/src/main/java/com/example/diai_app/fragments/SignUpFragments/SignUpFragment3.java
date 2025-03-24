package com.example.diai_app.fragments.SignUpFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.example.diai_app.dataModels.User;
import com.example.diai_app.dtos.BloodSugarRecordDTO;
import com.example.diai_app.dtos.ProfileDTO;
import com.example.diai_app.dtos.UserDTO;
import com.example.diai_app.fragments.BaseFragment;
import com.example.diai_app.HomeActivity;
import com.example.diai_app.manager.FirebaseDatabaseManager;
import com.example.diai_app.manager.UserManager;
import com.example.diai_app.R;
import com.example.diai_app.settings.DataConverter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SignUpFragment3 extends BaseFragment {
    private Spinner spinnerDiabetesType, spinnerAdditionInfo;
    private CheckBox checkFamilyHistory;
    private AppCompatButton btnStart;
    private ImageView btnBack;
    private FirebaseDatabaseManager db;

    public SignUpFragment3() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Gán danh sách loại tiểu đường vào Spinner
        ArrayAdapter<CharSequence> diabetesAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.diabetes_types, android.R.layout.simple_spinner_item);
        diabetesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiabetesType.setAdapter(diabetesAdapter);

        // Gán danh sách mục tiêu (Additional Info) vào Spinner
        ArrayAdapter<CharSequence> additionInfoAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.goal_array, android.R.layout.simple_spinner_item);
        additionInfoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdditionInfo.setAdapter(additionInfoAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_signup3;
    }

    @Override
    protected void bindView(View view) {
        spinnerDiabetesType = view.findViewById(R.id.spinnerDiabetesType);
        spinnerAdditionInfo = view.findViewById(R.id.spinnerAdditionInfo);
        checkFamilyHistory = view.findViewById(R.id.checkFamilyHistory);
        btnStart = view.findViewById(R.id.btnStart);
        btnBack = view.findViewById(R.id.btnBack3);
        db = FirebaseDatabaseManager.getInstance();
    }

    @Override
    protected void addOnEventListener() {
        // Xử lý nút Back
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
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
                String sex = bundle.getString("sex", "");
                // Chuyển đổi thành số
                int age = 0;
                double height = 0;
                double weight = 0.0;
                try {
                    age = Integer.parseInt(bundle.getString("age", "0"));
                    height = Double.parseDouble(bundle.getString("height", "0.0"));
                    weight = Double.parseDouble(bundle.getString("weight", "0.0").replace("kg", "").trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Invalid number format.", Toast.LENGTH_SHORT).show();
                    Log.d(SignUpFragment3.class.getName(), "NumberFormatException: " + e.toString());
                    return; // Ngừng tiến trình đăng ký nếu có lỗi chuyển đổi
                }
                // Tạo ProfileDTO
                ProfileDTO profile = new ProfileDTO(fullName, age, sex, weight, height, diabetesType, hasFamilyHistory, additionInfo);
                // Tạo danh sách BloodSugarRecordsDTO (Ban đầu chưa có dữ liệu, nên để danh sách rỗng)
                List<BloodSugarRecordDTO> bloodSugarRecords = new ArrayList<>();
                // Sinh UserID (có thể dùng timestamp hoặc Firebase UID sau)
                int userId = (int) (System.currentTimeMillis() / 1000);
                // Tạo UserDTO
                UserDTO userDTO = new UserDTO(userId, name, email, password, profile, bloodSugarRecords);
                db.createUser(userDTO, new FirebaseDatabaseManager.OnDataWriteListener() {
                    @Override
                    public void onSuccess() {
                        // Lưu thông tin user vào SharedPreferences
                        Gson gson = new Gson();
                        User user = DataConverter.convertToUserDataModel(userDTO);
                        String userJson = gson.toJson(user);
                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("loggedInUser", userJson);
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();
                        // Chuyển sang HomeActivity sau khi đăng ký thành công
                        Intent intent = new Intent(requireActivity(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(requireContext(), "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(requireContext(), "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
