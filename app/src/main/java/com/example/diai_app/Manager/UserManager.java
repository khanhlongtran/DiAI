package com.example.diai_app.Manager;

import com.example.diai_app.DataModel.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static UserManager instance;
    private final List<User> users;

    // Private constructor để sử dụng Singleton Pattern
    private UserManager() {
        users = new ArrayList<>();
        seedData();
    }

    // Sử dụng Singleton Pattern để đảm bảo chỉ có một instance của UserManager
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    // Thêm người dùng mới
    public void addUser(User user) {
        users.add(user);
    }

    public void seedData() {
        if (users.isEmpty()) {
            users.add(new User("tuanAnh", "12345", "tuan@example.com", "Male", 70.0, "Nguyễn Tuấn Anh", 28, 170.0, "Type 1", "No additional info", false));
            users.add(new User("minhHang", "abcdef", "hang@example.com", "Female", 55.0, "Trần Minh Hằng", 24, 160.0, "Type 2", "No additional info", true));
            users.add(new User("baoLong", "pass789", "long@example.com", "Male", 80.0, "Lê Bảo Long", 32, 180.0, "Other", "Has allergy to peanuts", false));
        }
    }


    public User authenticate(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user; // Trả về người dùng nếu thông tin hợp lệ
            }
        }
        return null; // Trả về null nếu thông tin không chính xác
    }


}

