package com.example.diai_app.dtos;

import com.google.firebase.database.PropertyName;

public class ProductDTO {
    @PropertyName("ProductID")
    private int productId;
    @PropertyName("ProductName")
    private String productName;
    @PropertyName("Description")
    private String description;
    @PropertyName("ProductImageUrl")
    private String productImageUrl;
    @PropertyName("Price")
    private double price;
    @PropertyName("category")
    private CategoryDTO category;

    // Constructor
    public ProductDTO(int id, String name, String description, String imageUrl, double price, CategoryDTO category) {
        this.productId = id;
        this.productName = name;
        this.description = description;
        this.productImageUrl = imageUrl;
        this.price = price;
        this.category = category;
    }

    public ProductDTO() {
        // Constructor rỗng cho ánh xạ từ Firebase
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + productId +
                ", name='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + productImageUrl + '\'' +
                ", price=" + price +
                ", category=" + category +
                '}';
    }
}