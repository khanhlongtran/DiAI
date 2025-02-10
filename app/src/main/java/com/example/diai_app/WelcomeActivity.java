package com.example.diai_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WelcomeActivity extends AppCompatActivity {
    private Button btnLogin, btnRegister;
    private ImageView ivWelcomeImage;
    private int[] imageResources = {R.drawable.mot, R.drawable.hai, R.drawable.ba}; // Mảng chứa các hình ảnh
    private int currentImageIndex = 0; // Chỉ số hình ảnh hiện tại
    private Handler handler = new Handler(); // Handler để điều khiển việc thay đổi hình ảnh


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // Ánh xạ các view
        ivWelcomeImage = findViewById(R.id.ivWelcomeImage);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        // Cập nhật hình ảnh mỗi 2 giây
        Runnable updateImageRunnable = new Runnable() {
            @Override
            public void run() {
                // Thay đổi hình ảnh
                ivWelcomeImage.setImageResource(imageResources[currentImageIndex]);

                // Cập nhật chỉ số hình ảnh cho lần thay đổi tiếp theo
                currentImageIndex = (currentImageIndex + 1) % imageResources.length;

                // Lập lại việc thay đổi hình ảnh sau 2 giây
                handler.postDelayed(this, 2000);
            }
        };

        // Bắt đầu thay đổi hình ảnh ngay khi app mở
        handler.post(updateImageRunnable);
        // Sự kiện khi bấm nút Đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình Đăng nhập (LoginActivity)
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Sự kiện khi bấm nút Đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình Đăng ký (RegisterActivity)
                Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}