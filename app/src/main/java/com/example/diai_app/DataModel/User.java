package com.example.diai_app.DataModel;

public class User {
    private String name;
    private String password;
    private String email;
    private String sex;
    private double weight;
    private String fullname;
    private int age;
    private double height;
    private String diabetesType;
    private String additionInfo;
    private boolean hasFamilyHistory; // Thêm thuộc tính này

    public User(String name, String password, String email, String sex, double weight, String fullname, int age, double height, String diabetesType, String additionInfo, boolean hasFamilyHistory) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.sex = sex;
        this.weight = weight;
        this.fullname = fullname;
        this.age = age;
        this.height = height;
        this.diabetesType = diabetesType;
        this.additionInfo = additionInfo;
        this.hasFamilyHistory = hasFamilyHistory;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getAdditionInfo() {
        return additionInfo;
    }

    public void setAdditionInfo(String additionInfo) {
        this.additionInfo = additionInfo;
    }

    public boolean isHasFamilyHistory() {
        return hasFamilyHistory;
    }

    public void setHasFamilyHistory(boolean hasFamilyHistory) {
        this.hasFamilyHistory = hasFamilyHistory;
    }
}

