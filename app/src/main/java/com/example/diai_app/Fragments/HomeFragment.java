package com.example.diai_app.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private TextView tvLatestBloodSugar, tvLatestBloodSugarTime, tvHello;
    private EditText etBloodSugar, etNotes;
    private ImageView ivProfile;
    private LineChart chartBloodSugar;
    private Button btnAddRecord;
    private LinearLayout layoutPreviousRecords;
    private List<BloodSugarRecord> bloodSugarRecords = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

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

        // Lấy name từ SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("loggedInUser", null);
        if (userJson != null) {
            Gson gson = new Gson();
            User loggedInUser = gson.fromJson(userJson, User.class);
            Log.d("TAGTAGTAG", "username: " + loggedInUser.getName());
            // Hiển thị lời chào
            tvHello.setText("Hello, " + loggedInUser.getName() + "!");
        }

        // Seed dữ liệu giả lập
        seedData();
        updateUI();
        // Xử lý sự kiện cho nút "Add New Record"
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
        return view;
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
    }

    private void seedData() {
        // Lấy SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        boolean isSeeded = sharedPreferences.getBoolean("isDataSeeded", false);

        // Kiểm tra nếu chưa seed thì tiến hành seed data
        if (!isSeeded) {
            bloodSugarRecords.add(new BloodSugarRecord(120, "Feb 5, 10:00 PM", "After dinner"));
            bloodSugarRecords.add(new BloodSugarRecord(130, "Feb 4, 09:30 AM", "Morning test"));

            // Đánh dấu là đã seed
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isDataSeeded", true);
            editor.apply();

            Log.d("TAGTAGTAG", "Seeding data...");
        } else {
            Log.d("TAGTAGTAG", "Data already seeded, skip seeding.");
        }
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
        for (int i = 0; i < bloodSugarRecords.size(); i++) {
            entries.add(new Entry(i, bloodSugarRecords.get(i).getLevel()));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Blood Sugar Level");
        dataSet.setColor(Color.RED);
        dataSet.setValueTextSize(12f);
        LineData lineData = new LineData(dataSet);
        chartBloodSugar.setData(lineData);
        chartBloodSugar.invalidate();
    }

}