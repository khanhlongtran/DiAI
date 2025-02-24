package com.example.diai_app.Fragments.ItemCaloriesFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diai_app.Adapter.RecipeAdapter;
import com.example.diai_app.DataModel.Recipe;
import com.example.diai_app.Fragments.BaseFragment;
import com.example.diai_app.R;

import java.util.ArrayList;
import java.util.List;

public class DinnerFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dinner;
    }

    @Override
    protected void bindView(View view) {
        recyclerView = view.findViewById(R.id.recipe_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Recipe> dinnerRecipes = getDinnerRecipes();
        adapter = new RecipeAdapter(getContext(), dinnerRecipes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void addOnEventListener() {
        // Hiện tại chưa có sự kiện nào cần lắng nghe, nếu có bạn có thể thêm vào đây
    }

    private List<Recipe> getDinnerRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("Green Salad", "20 min", "230", R.drawable.mot));
        // Thêm các công thức món ăn khác ở đây
        return recipes;
    }
}
