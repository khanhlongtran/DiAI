package com.example.diai_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY = 1; // 7 giây
    private TextView quoteText;
    private String[] quotes = {
            "The best way to predict the future is to create it.",
            "Success is not the key to happiness. Happiness is the key to success."
    };
    private int quoteIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        quoteText = findViewById(R.id.quote_text);
        updateQuote(); // Hiển thị câu đầu tiên

        new Handler().postDelayed(() -> {
            updateQuote(); // Hiển thị câu thứ hai sau 3 giây
        }, 3000);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            finish();
        }, SPLASH_DELAY);
    }

    private void updateQuote() {
        if (quoteIndex < quotes.length) {
            quoteText.setText(quotes[quoteIndex]);
            quoteIndex++;
        }
    }
}