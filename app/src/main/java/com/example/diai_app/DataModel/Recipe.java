package com.example.diai_app.DataModel;

public class Recipe {
    public String title;
    public String time;
    public String calories;
    public int imageResId; // Add image resource ID

    public Recipe(String title, String time, String calories, int imageResId) {
        this.title = title;
        this.time = time;
        this.calories = calories;
        this.imageResId = imageResId;
    }
}