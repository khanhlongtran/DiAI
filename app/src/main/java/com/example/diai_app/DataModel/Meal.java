package com.example.diai_app.DataModel;

import java.util.List;

public class Meal {
    private String name;
    private List<FoodItem> foodItems;

    public Meal(String name, List<FoodItem> foodItems) {
        this.name = name;
        this.foodItems = foodItems;
    }

    public String getName() {
        return name;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }
}
