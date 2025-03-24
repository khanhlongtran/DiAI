package com.example.diai_app.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.diai_app.dataModels.User;
import com.example.diai_app.LoginActivity;
import com.example.diai_app.R;
import com.example.diai_app.manager.FirebaseDatabaseManager;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends BaseFragment {

    private EditText etFullName, etAge, etWeight, etHeight, etAdditionInfo;
    private Spinner spinnerSex, spinnerDiabetesType;
    private CheckBox checkFamilyHistory;
    private Button btnUpdateProfile, btnLogout;
    private ImageView btnBack4;
    User loggedInUser;
    SharedPreferences sharedPreferences;
    private FirebaseDatabaseManager dbManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillData();
        setupSpinners();
    }

    @Override
    protected void addOnEventListener() {
        btnBack4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });
        // Xử lý sự kiện cập nhật hồ sơ
        btnUpdateProfile.setOnClickListener(v -> updateProfile());
        // Xử lý sự kiện Đăng xuất
        btnLogout.setOnClickListener(v -> {
            // Xóa dữ liệu trong SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("loggedInUser");
            editor.remove("isLoggedIn");
            editor.apply();

            // Chuyển về LoginActivity
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void fillData() {
        // Lấy name từ SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("loggedInUser", null);
        if (userJson != null) {
            Gson gson = new Gson();
            loggedInUser = gson.fromJson(userJson, User.class);
            Log.d("TAGTAGTAG", "ProfileFragment username: " + loggedInUser.getName());
        }
        // Điền dữ liệu vào các EditText
        etFullName.setText(loggedInUser.getFullname());
        etAge.setText(String.valueOf(loggedInUser.getAge()));
        etWeight.setText(String.valueOf(loggedInUser.getWeight()));
        etHeight.setText(String.valueOf(loggedInUser.getHeight()));
        etAdditionInfo.setText(loggedInUser.getAdditionInfo());
        checkFamilyHistory.setChecked(loggedInUser.isHasFamilyHistory()); // Thêm dòng này
        // Điền dữ liệu vào Spinner Giới tính
        if (loggedInUser.getSex() != null) {
            switch (loggedInUser.getSex()) {
                case "Male":
                    spinnerSex.setSelection(0);
                    break;
                case "Female":
                    spinnerSex.setSelection(1);
                    break;
                case "Other":
                    spinnerSex.setSelection(2);
                    break;
            }
        }

        // Điền dữ liệu vào Spinner Loại tiểu đường
        if (loggedInUser.getDiabetesType() != null) {
            switch (loggedInUser.getDiabetesType()) {
                case "Type 1":
                    spinnerDiabetesType.setSelection(0);
                    break;
                case "Type 2":
                    spinnerDiabetesType.setSelection(1);
                    break;
                case "Other":
                    spinnerDiabetesType.setSelection(2);
                    break;
            }
        }
    }

    @Override
    protected void bindView(View view) {
        etFullName = view.findViewById(R.id.etFullName);
        etAge = view.findViewById(R.id.etAge);
        etWeight = view.findViewById(R.id.etWeight);
        etHeight = view.findViewById(R.id.etHeight);
        etAdditionInfo = view.findViewById(R.id.etAdditionInfo);
        checkFamilyHistory = view.findViewById(R.id.checkFamilyHistory);
        spinnerSex = view.findViewById(R.id.spinnerSex);
        spinnerDiabetesType = view.findViewById(R.id.spinnerDiabetesType);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        btnBack4 = view.findViewById(R.id.btnBack4);
        btnLogout = view.findViewById(R.id.btnLogout);
        dbManager = FirebaseDatabaseManager.getInstance();
    }


    private void setupSpinners() {
        // Giới tính
        ArrayAdapter<CharSequence> sexAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sex_options, // Định nghĩa trong strings.xml
                android.R.layout.simple_spinner_item
        );
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSex.setAdapter(sexAdapter);

        // Loại tiểu đường
        ArrayAdapter<CharSequence> diabetesAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.diabetes_types, // Định nghĩa trong strings.xml
                android.R.layout.simple_spinner_item
        );
        diabetesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiabetesType.setAdapter(diabetesAdapter);
    }

    private void updateProfile() {
        // Thông tin cần cập nhật
        String fullName = etFullName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String weight = etWeight.getText().toString().trim();
        String height = etHeight.getText().toString().trim();
        String additionInfo = etAdditionInfo.getText().toString().trim();
        String gender = spinnerSex.getSelectedItem().toString();
        String diabetesType = spinnerDiabetesType.getSelectedItem().toString();
        boolean hasFamilyHistory = checkFamilyHistory.isChecked();

        if (fullName.isEmpty() || age.isEmpty() || weight.isEmpty() || height.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy thông tin user từ SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("loggedInUser", null);

        if (userJson != null) {
            Gson gson = new Gson();
            User loggedInUser = gson.fromJson(userJson, User.class);
            String userEmail = loggedInUser.getEmail(); // Lấy email để update

            // Tạo Map chứa các trường cần update
            Map<String, Object> updates = new HashMap<>();
            updates.put("profile/fullName", fullName);
            updates.put("profile/age", Integer.parseInt(age));
            updates.put("profile/weight", Double.parseDouble(weight));
            updates.put("profile/height", Double.parseDouble(height));
            updates.put("profile/additionInfo", additionInfo);
            updates.put("profile/sex", gender);
            updates.put("profile/diabetesType", diabetesType);
            updates.put("profile/hasFamilyHistory", hasFamilyHistory);


            dbManager.updateUserByEmail(userEmail, updates, new FirebaseDatabaseManager.OnDataUpdateListener() {
                @Override
                public void onSuccess() {
                    // Cập nhật thông tin mới để lưu
                    loggedInUser.setFullname(fullName);
                    loggedInUser.setAge(Integer.parseInt(age));
                    loggedInUser.setWeight(Double.parseDouble(weight));
                    loggedInUser.setHeight(Double.parseDouble(height));
                    loggedInUser.setAdditionInfo(additionInfo);
                    loggedInUser.setSex(gender);
                    loggedInUser.setDiabetesType(diabetesType);
                    loggedInUser.setHasFamilyHistory(hasFamilyHistory);

                    // Chuyển đối tượng User thành chuỗi JSON
                    String updatedUserJson = gson.toJson(loggedInUser);

                    // Lưu lại vào SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("loggedInUser", updatedUserJson);
                    editor.apply();

                    Toast.makeText(requireContext(), "Hồ sơ đã được cập nhật!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(requireContext(), "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(requireContext(), "Không thể cập nhật thông tin người dùng.", Toast.LENGTH_SHORT).show();
        }
    }
}
