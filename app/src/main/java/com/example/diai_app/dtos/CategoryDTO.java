package com.example.diai_app.dtos;

import com.google.firebase.database.PropertyName;

public class CategoryDTO {
    @PropertyName("CategoryID")
    private int categoryID;
    @PropertyName("CategoryName")
    private String categoryName;
    @PropertyName("ImageUrl")
    private String imageUrl;

    public CategoryDTO() {
    }

    // Constructor
    public CategoryDTO(int id, String name, String imageUrl) {
        this.categoryID = id;
        this.categoryName = name;
        this.imageUrl = imageUrl;
    }

    // Getters
    public int getId() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}