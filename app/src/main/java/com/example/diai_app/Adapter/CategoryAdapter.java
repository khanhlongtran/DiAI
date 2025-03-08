package com.example.diai_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diai_app.DataModel.Category;
import com.example.diai_app.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categoryList;

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryTextView.setText(category.getName());

        // Đặt ảnh theo vị trí
        int imageResId;
        switch (position + 1) { // Vì position bắt đầu từ 0
            case 1:
                imageResId = R.drawable.cat1;
                break;
            case 2:
                imageResId = R.drawable.cat2;
                break;
            case 3:
                imageResId = R.drawable.cat3;
                break;
            case 4:
                imageResId = R.drawable.cat4;
                break;
            case 5:
                imageResId = R.drawable.cat5;
                break;
            case 6:
                imageResId = R.drawable.cat6;
                break;
            case 7:
                imageResId = R.drawable.cat7;
                break;
            case 8:
                imageResId = R.drawable.cat8;
                break;
            default:
                imageResId = R.drawable.cat1;
                break;
        }
        holder.categoryImg.setImageResource(imageResId);
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView;
        ImageView categoryImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.categoryText);
            categoryImg = itemView.findViewById(R.id.categoryImg);
        }
    }
}
