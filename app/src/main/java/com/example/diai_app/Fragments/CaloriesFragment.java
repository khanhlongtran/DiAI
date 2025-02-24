package com.example.diai_app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.diai_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CaloriesFragment extends BaseFragment {
    private final OkHttpClient client = new OkHttpClient();
    private LinearLayout mealContainer;
    private Button btnGenerateMeals;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_calories;
    }

    @Override
    protected void bindView(View view) {
        mealContainer = view.findViewById(R.id.mealContainer);
        btnGenerateMeals = view.findViewById(R.id.btn_generate_meals);
    }

    @Override
    protected void addOnEventListener() {
        btnGenerateMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAPI();
            }
        });
    }

    private void callAPI() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("input", "Suggest meals with detailed nutrition info. Please return only json array meal format without anything else like \"```json and ```");
            jsonBody.put("context", "The AI suggests meal plans with details for each meal, including food items and their nutritional values. Return using only JSON format for me to use JSONObject and without anything else like \"```json\" and \"```\" ");

            JSONArray historyArray = new JSONArray();
            JSONObject previousMessage = new JSONObject();
            previousMessage.put("input", "Suggest meals with detailed nutrition info.");
            previousMessage.put("response", "[{\"meal\":\"Breakfast\",\"items\":[{\"name\":\"Oatmeal\",\"calories\":150,\"fat\":3,\"protein\":5,\"carbs\":27},{\"name\":\"Scrambled Eggs\",\"calories\":200,\"fat\":14,\"protein\":12,\"carbs\":2}]},{\"meal\":\"Lunch\",\"items\":[{\"name\":\"Grilled Chicken\",\"calories\":300,\"fat\":8,\"protein\":40,\"carbs\":2},{\"name\":\"Brown Rice\",\"calories\":250,\"fat\":2,\"protein\":6,\"carbs\":52}]},{\"meal\":\"Dinner\",\"items\":[{\"name\":\"Salmon\",\"calories\":350,\"fat\":20,\"protein\":40,\"carbs\":0},{\"name\":\"Steamed Vegetables\",\"calories\":100,\"fat\":1,\"protein\":3,\"carbs\":20}]}]");
            historyArray.put(previousMessage);
            jsonBody.put("history", historyArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url("https://api.nlpcloud.io/v1/gpu/finetuned-llama-3-70b/chatbot")
                .header("Authorization", "Token 754352d8634ecf66b2dcde7f8f9920561fc58944")
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getActivity(), "Failed to load data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        // Get the full response string
                        String responseString = response.body().string();
                        Log.d("TAG", responseString);

                        // Parse the outer JSON object
                        JSONObject jsonResponse = new JSONObject(responseString);

                        // Extract the string from the "response" key
                        String mealsJsonString = jsonResponse.getString("response");

                        // Now parse the mealsJsonString into a JSON array
                        JSONArray mealsArray = new JSONArray(mealsJsonString);

                        requireActivity().runOnUiThread(() -> displayMeals(mealsArray));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
    }

    private void displayMeals(JSONArray mealsArray) {
        mealContainer.removeAllViews();

        for (int i = 0; i < mealsArray.length(); i++) {
            try {
                JSONObject meal = mealsArray.getJSONObject(i);
                String mealName = meal.getString("meal");

                JSONArray items = meal.getJSONArray("items");
                int totalCalories = 0;
                for (int j = 0; j < items.length(); j++) {
                    totalCalories += items.getJSONObject(j).getInt("calories");
                }

                View mealView = LayoutInflater.from(getActivity()).inflate(R.layout.item_meal, mealContainer, false);
                TextView mealTitle = mealView.findViewById(R.id.mealTitle);
                TextView mealCalories = mealView.findViewById(R.id.mealCalories);
                ImageView mealImage = mealView.findViewById(R.id.mealImage);
                // Lấy nội dung của TextView
                String mealType = mealTitle.getText().toString();

                // Kiểm tra loại bữa ăn và gán hình ảnh tương ứng
                if (mealType.equalsIgnoreCase("Breakfast")) {
                    mealImage.setImageResource(R.drawable.breakfast);
                } else if (mealType.equalsIgnoreCase("Lunch")) {
                    mealImage.setImageResource(R.drawable.lunch);
                } else if (mealType.equalsIgnoreCase("Dinner")) {
                    mealImage.setImageResource(R.drawable.dinner);
                } else {
                    mealImage.setImageResource(R.drawable.mot); // Hình ảnh mặc định nếu không khớp
                }

                mealTitle.setText(mealName);
                mealCalories.setText(totalCalories + " Kcal");

                mealView.setOnClickListener(v -> showMealDetail(meal));

                mealContainer.addView(mealView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showMealDetail(JSONObject meal) {
        MealDetailFragment mealDetailFragment = new MealDetailFragment();

        // Truyền dữ liệu sang Fragment
        Bundle bundle = new Bundle();
        bundle.putString("mealData", meal.toString());
        mealDetailFragment.setArguments(bundle);

        // Mở Fragment mới
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, mealDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}