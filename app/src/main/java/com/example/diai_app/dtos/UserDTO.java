package com.example.diai_app.dtos;

import com.google.firebase.database.PropertyName;

import java.util.List;

import java.util.List;

public class UserDTO {
    @PropertyName("UserID")
    private int userId;
    @PropertyName("Username")
    private String username;
    @PropertyName("Email")
    private String email;
    @PropertyName("PasswordHash")
    private String passwordHash; // Thêm để kiểm tra đăng nhập
    @PropertyName("profile")
    private ProfileDTO profile;
    @PropertyName("bloodSugarRecords")
    private List<BloodSugarRecordDTO> bloodSugarRecords;

    public UserDTO(int userId, String username, String email, String passwordHash, ProfileDTO profile, List<BloodSugarRecordDTO> bloodSugarRecords) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.profile = profile;
        this.bloodSugarRecords = bloodSugarRecords;
    }

    public UserDTO() {
    }

    // Getters và Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

    public List<BloodSugarRecordDTO> getBloodSugarRecords() {
        return bloodSugarRecords;
    }

    public void setBloodSugarRecords(List<BloodSugarRecordDTO> bloodSugarRecords) {
        this.bloodSugarRecords = bloodSugarRecords;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", profile=" + profile +
                ", bloodSugarRecords=" + bloodSugarRecords +
                '}';
    }

}