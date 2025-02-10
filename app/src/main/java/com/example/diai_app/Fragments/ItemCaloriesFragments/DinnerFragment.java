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
import com.example.diai_app.R;

import java.util.ArrayList;
import java.util.List;

public class DinnerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dinner, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recipe_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Recipe> dinnerRecipes = getDinnerRecipes(); // Implement this
        RecipeAdapter adapter = new RecipeAdapter(getContext(), dinnerRecipes);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<Recipe> getDinnerRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("Green Salad", "20 min", "230", R.drawable.mot)); // Replace with your image resources
        // Add more dinner recipes here
        return recipes;
    }
}