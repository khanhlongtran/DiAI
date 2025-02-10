package com.example.diai_app.DataModel;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private String category;
    private double price;
    private int imageResId;

    public Product(int id, String name, String category, double price, int imageResId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }
}
