package com.example.diai_app.DataModel;

import java.util.List;

public class DayMeals {
    private String date;
    private List<Meal> meals;

    public DayMeals(String date, List<Meal> meals) {
        this.date = date;
        this.meals = meals;
    }

    public String getDate() {
        return date;
    }

    public List<Meal> getMeals() {
        return meals;
    }
}