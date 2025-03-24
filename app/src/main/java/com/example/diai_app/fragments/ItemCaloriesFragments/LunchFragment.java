package com.example.diai_app.fragments.ItemCaloriesFragments;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diai_app.adapters.RecipeAdapter;
import com.example.diai_app.dataModels.Recipe;
import com.example.diai_app.fragments.BaseFragment;
import com.example.diai_app.R;

import java.util.ArrayList;
import java.util.List;

public class LunchFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_lunch;
    }

    @Override
    protected void bindView(View view) {
        recyclerView = view.findViewById(R.id.recipe_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Recipe> lunchRecipes = getLunchRecipes();
        adapter = new RecipeAdapter(getContext(), lunchRecipes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void addOnEventListener() {
        // Hiện tại chưa có sự kiện nào cần lắng nghe, nếu có bạn có thể thêm vào đây
    }

    private List<Recipe> getLunchRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("Meal Salad", "25 min", "220", R.drawable.mot));
        // Thêm các công thức món ăn khác ở đây
        return recipes;
    }
}
