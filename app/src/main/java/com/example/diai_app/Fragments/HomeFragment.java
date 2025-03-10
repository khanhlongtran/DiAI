package com.example.diai_app.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diai_app.DataModel.BloodSugarRecord;
import com.example.diai_app.DataModel.User;
import com.example.diai_app.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends BaseFragment {
    private TextView tvLatestBloodSugar, tvLatestBloodSugarTime, tvHello, tvCurrentWeight, tvCurrentHeight, tvCurrentBMI;
    private EditText etBloodSugar, etNotes;
    private ImageView ivProfile;
    private LineChart chartBloodSugar;
    private AppCompatButton btnAddRecord;
    private LinearLayout layoutPreviousRecords;
    private List<BloodSugarRecord> bloodSugarRecords = new ArrayList<>();

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("records", (Serializable) bloodSugarRecords);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Lấy name từ SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("loggedInUser", null);
        if (userJson != null) {
            Gson gson = new Gson();
            User loggedInUser = gson.fromJson(userJson, User.class);
            Log.d("TAGTAGTAG", "username: " + loggedInUser.getName());
            // Hiển thị lời chào
            tvHello.setText("Hello, " + loggedInUser.getName() + "!");
            // Cập nhật cân nặng & chiều cao từ object
            tvCurrentWeight.setText(loggedInUser.getWeight() + " kg");
            tvCurrentHeight.setText(loggedInUser.getHeight() + " cm");
        }
        if (savedInstanceState != null) {
            bloodSugarRecords = (List<BloodSugarRecord>) savedInstanceState.getSerializable("records");
            updateChart();  // Cập nhật lại biểu đồ
        }
        Log.d("HomeFragment", "onViewCreated: bloodSugarRecords " + (bloodSugarRecords.size()));
        seedData();
        updateUI();
        calculateBMI();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void bindView(View view) {
        // Ánh xạ các view bằng findViewById
        tvLatestBloodSugar = view.findViewById(R.id.tv_latest_blood_sugar);
        tvLatestBloodSugarTime = view.findViewById(R.id.tv_latest_blood_sugar_time);
        tvHello = view.findViewById(R.id.tvHello);
        ivProfile = view.findViewById(R.id.ivProfile);
        etBloodSugar = view.findViewById(R.id.et_blood_sugar);
        etNotes = view.findViewById(R.id.et_notes);
        chartBloodSugar = view.findViewById(R.id.chart_blood_sugar);
        btnAddRecord = view.findViewById(R.id.btn_add_record);
        layoutPreviousRecords = view.findViewById(R.id.layout_previous_records);
        tvCurrentHeight = view.findViewById(R.id.tv_current_height);
        tvCurrentWeight = view.findViewById(R.id.tv_current_weight);
        tvCurrentBMI = view.findViewById(R.id.tv_current_BMI);
    }

    @Override
    protected void addOnEventListener() {
        btnAddRecord.setOnClickListener(v -> {
            addNewRecord();
            Toast.makeText(getContext(), "Add New Record Clicked", Toast.LENGTH_SHORT).show();
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the new fragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, new ProfileFragment()); // Replace with your fragment container ID
                transaction.addToBackStack(null); // Add to back stack for navigation
                transaction.commit();
            }
        });

        // Thêm sự kiện Long Click để sửa cân nặng và chiều cao
        tvCurrentWeight.setOnLongClickListener(v -> {
            showEditDialog(tvCurrentWeight, "Cập nhật cân nặng", "kg");
            return true; // Trả về true để xử lý long click
        });

        tvCurrentHeight.setOnLongClickListener(v -> {
            showEditDialog(tvCurrentHeight, "Cập nhật chiều cao", "cm");
            return true;
        });

    }

    private void calculateBMI() {
        SharedPreferences sharedPreferences = requireActivity()
                .getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("loggedInUser", null);

        if (userJson != null) {
            Gson gson = new Gson();
            User loggedInUser = gson.fromJson(userJson, User.class);

            double weight = loggedInUser.getWeight(); // Lấy cân nặng
            double height = loggedInUser.getHeight() / 100.0; // Chuyển cm -> mét

            if (weight > 0 && height > 0) {
                double bmi = weight / (height * height);
                String bmiCategory = getBMICategory(bmi);

                tvCurrentBMI.setText(String.format(" %.1f (%s)", bmi, bmiCategory));
            } else {
                tvCurrentBMI.setText("BMI: Chưa có dữ liệu");
            }
        }
    }

    // Hàm phân loại BMI
    private String getBMICategory(double bmi) {
        if (bmi < 18.5) return "Gầy";
        if (bmi < 24.9) return "Bình thường";
        if (bmi < 29.9) return "Thừa cân";
        return "Béo phì";
    }

    private void showEditDialog(TextView textView, String title, String unit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(textView.getContext());
        builder.setTitle(title);

        // Tạo EditText để nhập giá trị mới
        final EditText input = new EditText(textView.getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setText(textView.getText().toString().replace(unit, "").trim());

        builder.setView(input);

        // Nút "Lưu"
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newValue = input.getText().toString().trim();
            if (!newValue.isEmpty()) {
                textView.setText(newValue + " " + unit); // Cập nhật UI

                // Lấy dữ liệu từ SharedPreferences
                SharedPreferences sharedPreferences = textView.getContext()
                        .getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String userJson = sharedPreferences.getString("loggedInUser", null);

                if (userJson != null) {
                    Gson gson = new Gson();
                    User loggedInUser = gson.fromJson(userJson, User.class);

                    // Cập nhật giá trị vào object
                    if (textView.getId() == R.id.tv_current_weight) {
                        loggedInUser.setWeight(Double.parseDouble(newValue));
                    } else if (textView.getId() == R.id.tv_current_height) {
                        loggedInUser.setHeight(Double.parseDouble(newValue));
                    }

                    // Chuyển đối tượng User thành JSON
                    String updatedUserJson = gson.toJson(loggedInUser);

                    // Lưu lại vào SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("loggedInUser", updatedUserJson);
                    editor.apply();
                }
            }
        });

        // Nút "Hủy"
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void addNewRecord() {
        String bloodSugarText = etBloodSugar.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (bloodSugarText.isEmpty()) {
            Toast.makeText(getContext(), "Please enter blood sugar level", Toast.LENGTH_SHORT).show();
            return;
        }

        int bloodSugarValue = Integer.parseInt(bloodSugarText);
        String currentTime = new SimpleDateFormat("MMM d, hh:mm a", Locale.getDefault()).format(new Date());

        bloodSugarRecords.add(0, new BloodSugarRecord(bloodSugarValue, currentTime, notes));
        updateUI();

        etBloodSugar.setText("");
        etNotes.setText("");
        saveBloodSugarRecords(bloodSugarRecords);
    }

    private void seedData() {
        bloodSugarRecords = loadBloodSugarRecords();
    }

    private void saveBloodSugarRecords(List<BloodSugarRecord> records) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(records); // Chuyển danh sách thành JSON
        editor.putString("bloodSugarRecords", json);
        editor.apply();
    }

    private List<BloodSugarRecord> loadBloodSugarRecords() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String json = sharedPreferences.getString("bloodSugarRecords", null);
        Log.d("HomeFragment", "loadBloodSugarRecords: loading " + bloodSugarRecords.size());
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<BloodSugarRecord>>() {
            }.getType();
            return gson.fromJson(json, type); // Chuyển JSON về danh sách
        }
        return new ArrayList<>(); // Trả về danh sách rỗng nếu không có dữ liệu
    }

    private void updateUI() {
        if (!bloodSugarRecords.isEmpty()) {
            BloodSugarRecord latestRecord = bloodSugarRecords.get(0);
            tvLatestBloodSugar.setText(latestRecord.getLevel() + " mg/dL");
            tvLatestBloodSugarTime.setText(latestRecord.getTime());
        }
        updateChart();
        updatePreviousRecords();
    }

    private void updatePreviousRecords() {
        layoutPreviousRecords.removeAllViews(); // Xóa danh sách cũ

        // Nếu danh sách có hơn 1 phần tử, hiển thị từ phần tử thứ 2 trở đi
        for (int i = 1; i < bloodSugarRecords.size(); i++) {
            BloodSugarRecord record = bloodSugarRecords.get(i);

            LinearLayout recordLayout = new LinearLayout(getContext());
            recordLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            recordLayout.setPadding(12, 12, 12, 12);
            recordLayout.setOrientation(LinearLayout.VERTICAL);
            recordLayout.setBackgroundColor(Color.parseColor("#FFF3E0"));
            ((LinearLayout.LayoutParams) recordLayout.getLayoutParams()).setMargins(0, 0, 8, 0);

            TextView tvValue = new TextView(getContext());
            tvValue.setText(record.getLevel() + " mg/dL");
            tvValue.setTextSize(16);
            tvValue.setTypeface(null, Typeface.BOLD);
            tvValue.setTextColor(Color.parseColor("#D32F2F"));

            TextView tvTime = new TextView(getContext());
            tvTime.setText(record.getTime());
            tvTime.setTextSize(14);
            tvTime.setTextColor(Color.parseColor("#757575"));

            recordLayout.addView(tvValue);
            recordLayout.addView(tvTime);
            layoutPreviousRecords.addView(recordLayout);
        }
    }


    private void updateChart() {
        List<Entry> entries = new ArrayList<>();
        for (int i = bloodSugarRecords.size() - 1, j = 0; i >= 0; i--, j++) {
            entries.add(new Entry(j, bloodSugarRecords.get(i).getLevel()));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Blood Sugar Level");
        dataSet.setColor(Color.RED);
        dataSet.setValueTextSize(12f);
        LineData lineData = new LineData(dataSet);
        chartBloodSugar.setData(lineData);
        chartBloodSugar.getDescription().setEnabled(false);
        chartBloodSugar.invalidate();
    }

}