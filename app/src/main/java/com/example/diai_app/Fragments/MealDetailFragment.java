package com.example.diai_app.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.diai_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MealDetailFragment extends BaseFragment {
    private LinearLayout mealDetailContainer;
    private ImageView btnBack5;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Lấy dữ liệu từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String mealData = bundle.getString("mealData");
            try {
                JSONObject meal = new JSONObject(mealData);
                displayMealDetails(meal);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meal_detail;
    }

    @Override
    protected void bindView(View view) {
        mealDetailContainer = view.findViewById(R.id.mealDetailContainer);
        btnBack5 = view.findViewById(R.id.btnBack5);
    }

    @Override
    protected void addOnEventListener() {
        btnBack5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });
    }

    private void displayMealDetails(JSONObject meal) {
        try {
            String mealName = meal.getString("meal"); // Lấy tên bữa ăn
            JSONArray items = meal.getJSONArray("items"); // Lấy danh sách món ăn (KHÔNG dùng getString)

            // Hiển thị tiêu đề bữa ăn
            TextView titleView = new TextView(getContext());
            titleView.setText(mealName);
            titleView.setTextSize(18);
            mealDetailContainer.addView(titleView);

            // Thêm hình ảnh phù hợp với từng loại bữa ăn
            ImageView mealImage = new ImageView(getContext());
            if (mealName.equalsIgnoreCase("Breakfast")) {
                mealImage.setImageResource(R.drawable.breakfast);
            } else if (mealName.equalsIgnoreCase("Lunch")) {
                mealImage.setImageResource(R.drawable.lunch);
            } else if (mealName.equalsIgnoreCase("Dinner")) {
                mealImage.setImageResource(R.drawable.dinner);
            }

            // Đặt kích thước ảnh (tùy chỉnh nếu cần)
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 10, 0, 10);
            mealImage.setLayoutParams(layoutParams);

            // Thêm ảnh vào giao diện
            mealDetailContainer.addView(mealImage);

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i); // Lấy từng món ăn
                String foodName = item.getString("name");
                int calories = item.getInt("calories");

                // CardView chứa từng món ăn
                CardView cardView = new CardView(getContext());
                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                cardParams.setMargins(20, 10, 20, 10);
                cardView.setLayoutParams(cardParams);
                cardView.setRadius(15);
                cardView.setCardElevation(6);
                cardView.setPadding(20, 20, 20, 20);

                // Layout chứa ảnh + text
                LinearLayout itemLayout = new LinearLayout(getContext());
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setGravity(Gravity.CENTER_VERTICAL);

                // Layout chứa text
                LinearLayout textLayout = new LinearLayout(getContext());
                textLayout.setOrientation(LinearLayout.VERTICAL);

                // Tên món ăn
                TextView foodNameView = new TextView(getContext());
                foodNameView.setText(foodName);
                foodNameView.setTextSize(16);
                foodNameView.setTypeface(null, Typeface.BOLD);
                foodNameView.setTextColor(Color.BLACK);

                // Lượng calo
                TextView caloriesView = new TextView(getContext());
                caloriesView.setText(calories + " Kcal");
                caloriesView.setTextSize(14);
                caloriesView.setTextColor(Color.GRAY);

                // Thêm các view vào layout
                textLayout.addView(foodNameView);
                textLayout.addView(caloriesView);
                itemLayout.addView(textLayout);
                cardView.addView(itemLayout);
                mealDetailContainer.addView(cardView);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
