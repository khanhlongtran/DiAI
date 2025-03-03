package com.example.diai_app.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diai_app.R;
import com.google.gson.Gson;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private final List<String> mealList;
    private final Context context; // Thêm context để lưu SharedPreferences

    public MealAdapter(Context context, List<String> mealList) {
        this.context = context;
        this.mealList = mealList;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_by_chat_bot, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        holder.mealName.setText(mealList.get(position));
        // Bắt sự kiện Long Click để xóa
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc muốn xóa món ăn này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        mealList.remove(position);
                        notifyItemRemoved(position);
                        saveMealsToPreferences(); // Cập nhật SharedPreferences
                    })
                    .setNegativeButton("Hủy", null)
                    .show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    // Hàm lưu danh sách meal vào SharedPreferences
    private void saveMealsToPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MealsByChatBotAI", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mealList);
        editor.putString("favorite_list", json);
        editor.apply();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealName;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.meal_name);
        }
    }
}
