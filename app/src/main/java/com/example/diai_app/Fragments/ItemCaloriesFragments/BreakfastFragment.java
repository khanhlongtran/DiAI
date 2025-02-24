package com.example.diai_app.Fragments.ItemCaloriesFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diai_app.Adapter.RecipeAdapter;
import com.example.diai_app.DataModel.Recipe;
import com.example.diai_app.Fragments.BaseFragment;
import com.example.diai_app.R;

import java.util.ArrayList;
import java.util.List;

public class BreakfastFragment extends BaseFragment {
    RecyclerView recyclerView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Recipe> breakfastRecipes = getBreakfastRecipes(); // Implement this
        RecipeAdapter adapter = new RecipeAdapter(getContext(), breakfastRecipes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_breakfast;
    }

    @Override
    protected void bindView(View view) {
        recyclerView = view.findViewById(R.id.recipe_recycler_view);

    }

    @Override
    protected void addOnEventListener() {
        super.addOnEventListener();
    }

    private List<Recipe> getBreakfastRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("Avocado Salad", "25 min", "230", R.drawable.mot)); // Replace with your image resources
        recipes.add(new Recipe("Breakfast Bowl", "25 min", "230", R.drawable.mot));
        // Add more breakfast recipes here
        return recipes;
    }
}
