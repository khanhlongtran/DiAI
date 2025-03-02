package com.example.diai_app.Fragments.SignUpFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diai_app.Fragments.BaseFragment;
import com.example.diai_app.R;

public class SignUpFragment2 extends BaseFragment {
    private EditText etFullName, etAge, etHeight, editTextWeight;
    private Spinner spinnerSex;
    private Button btnNext;
    private ImageView btnBack;

    public SignUpFragment2() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Gán danh sách Giới tính (Sex) vào Spinner
        ArrayAdapter<CharSequence> sexAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.sex_options, android.R.layout.simple_spinner_item);
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSex.setAdapter(sexAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_signup2;
    }

    @Override
    protected void bindView(View view) {
        etFullName = view.findViewById(R.id.etFullName);
        etAge = view.findViewById(R.id.etAge);
        etHeight = view.findViewById(R.id.etHeight);
        spinnerSex = view.findViewById(R.id.spinnerSex);
        editTextWeight = view.findViewById(R.id.editTextWeight);
        btnNext = view.findViewById(R.id.btnNext);
        btnBack = view.findViewById(R.id.btnBack2);
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
            String fullName = etFullName.getText().toString();
            String age = etAge.getText().toString();
            String height = etHeight.getText().toString();
            String sex = spinnerSex.getSelectedItem().toString();
            String weight = editTextWeight.getText().toString().trim();
            // Chuyển sang SignupFragment3
            SignUpFragment3 fragment3 = new SignUpFragment3();
            Bundle bundle = getArguments(); // Lấy dữ liệu cũ từ Fragment1
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString("fullName", fullName);
            bundle.putString("age", age);
            bundle.putString("height", height);
            bundle.putString("sex", sex);
            bundle.putString("weight", weight);
            fragment3.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment3)
                    .addToBackStack(null)
                    .commit();
        });
    }
}
