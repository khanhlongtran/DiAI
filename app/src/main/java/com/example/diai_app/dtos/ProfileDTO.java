package com.example.diai_app.dtos;

import com.google.firebase.database.PropertyName;

public class ProfileDTO {
    @PropertyName("FullName")
    private String fullName;
    @PropertyName("Age")
    private int age;
    @PropertyName("Sex")
    private String sex;
    @PropertyName("Weight")
    private double weight;
    @PropertyName("Height")
    private double height;
    @PropertyName("DiabetesType")
    private String diabetesType;
    @PropertyName("FamilyHistory")
    private boolean familyHistory;
    @PropertyName("AdditionalInfo")
    private String additionalInfo;

    // Constructor
    public ProfileDTO(String fullName, int age, String sex, double weight, double height,
                      String diabetesType, boolean familyHistory, String additionalInfo) {
        this.fullName = fullName;
        this.age = age;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
        this.diabetesType = diabetesType;
        this.familyHistory = familyHistory;
        this.additionalInfo = additionalInfo;
    }

    public ProfileDTO() {
        // Constructor rỗng cho ánh xạ từ Firebase
    }

    // Getters và Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getDiabetesType() {
        return diabetesType;
    }

    public void setDiabetesType(String diabetesType) {
        this.diabetesType = diabetesType;
    }

    public boolean isFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(boolean familyHistory) {
        this.familyHistory = familyHistory;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

}