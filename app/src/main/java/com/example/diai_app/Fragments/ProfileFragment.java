package com.example.diai_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diai_app.R;

public class ProfileFragment extends Fragment {

    private EditText etFullName, etAge, etWeight, etHeight, etAdditionInfo;
    private Spinner spinnerSex, spinnerDiabetesType;
    private CheckBox checkFamilyHistory;
    private Button btnUpdateProfile;
    private ImageView btnBack4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Ánh xạ view
        etFullName = view.findViewById(R.id.etFullName);
        etAge = view.findViewById(R.id.etAge);
        etWeight = view.findViewById(R.id.etWeight);
        etHeight = view.findViewById(R.id.etHeight);
        etAdditionInfo = view.findViewById(R.id.etAdditionInfo);
        spinnerSex = view.findViewById(R.id.spinnerSex);
        spinnerDiabetesType = view.findViewById(R.id.spinnerDiabetesType);
        checkFamilyHistory = view.findViewById(R.id.checkFamilyHistory);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        btnBack4 = view.findViewById(R.id.btnBack4);
        // Khởi tạo Spinner
        setupSpinners();
        btnBack4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });
        // Xử lý sự kiện cập nhật hồ sơ
        btnUpdateProfile.setOnClickListener(v -> updateProfile());

        return view;
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

        // Gửi dữ liệu cập nhật (giả lập)
        Toast.makeText(requireContext(), "Hồ sơ đã được cập nhật!", Toast.LENGTH_SHORT).show();
    }
}
