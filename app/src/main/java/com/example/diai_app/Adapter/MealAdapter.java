package com.example.diai_app.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
        holder.degreeTxt.setText(String.valueOf(position + 1)); // Vị trí bắt đầu từ 1
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
        TextView mealName, degreeTxt, scoreTxt;
        RatingBar ratingBar;
        boolean isExpanded = false; // Biến trạng thái xem có đang mở rộng hay không

        @SuppressLint("ClickableViewAccessibility")
        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.meal_name);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            degreeTxt = itemView.findViewById(R.id.degreeTxt);
            scoreTxt = itemView.findViewById(R.id.scoreTxt);
            // Xử lý sự kiện click để mở rộng / thu gọn
            mealName.setOnClickListener(v -> {
                if (isExpanded) {
                    mealName.setMaxLines(2); // Thu gọn về 2 dòng
                    mealName.setEllipsize(TextUtils.TruncateAt.END);
                } else {
                    mealName.setMaxLines(Integer.MAX_VALUE); // Hiển thị toàn bộ
                    mealName.setEllipsize(null);
                }
                isExpanded = !isExpanded; // Đảo trạng thái
            });
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        Toast.makeText(itemView.getContext(), "Bạn đã đánh giá: " + rating + " sao", Toast.LENGTH_SHORT).show();
                        scoreTxt.setText(String.valueOf(rating)); // Cập nhật điểm vào scoreTxt
                    }
                }
            });
        }
    }
}
