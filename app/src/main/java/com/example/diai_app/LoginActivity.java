package com.example.diai_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diai_app.DataModel.User;
import com.example.diai_app.Fragments.ForgotPasswordFragment;
import com.example.diai_app.Manager.UserManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignup, tvForgotPassword;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;
    FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        googleBtn = findViewById(R.id.google_btn);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            navigateToSecondActivity();
        }


        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        Log.d("LoginActivity", "onCreate: " + isLoggedIn);
        if (isLoggedIn) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        // Ánh xạ các view
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Xử lý sự kiện khi bấm Đăng nhập

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Kiểm tra rỗng
                if (email.isEmpty()) {
                    etEmail.setError("Please enter your email");
                    etEmail.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    etPassword.setError("Please enter your password");
                    etPassword.requestFocus();
                    return;
                }

                // Kiểm tra thông tin đăng nhập
                User user = UserManager.getInstance().authenticate(email, password);
                if (user != null) {
                    Gson gson = new Gson();
                    String userJson = gson.toJson(user);

                    // Lưu thông tin người dùng vào SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("loggedInUser", userJson);
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    // Nếu thông tin đúng, chuyển sang màn hình chính
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Nếu sai, hiển thị lỗi cụ thể và hiệu ứng rung
                    etPassword.setError("Incorrect password");
                    etPassword.requestFocus();
                    shakeEditText(etPassword);
                }
            }
        });

        // Xử lý sự kiện khi bấm vào "Chưa có tài khoản? Đăng ký"
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        tvForgotPassword.setOnClickListener(v -> {
            findViewById(R.id.scrollView2).setVisibility(View.GONE);
            findViewById(R.id.constraintLayout2).setVisibility(View.GONE);
            fragmentContainer = findViewById(R.id.fragment_container);
            fragmentContainer.setVisibility(View.VISIBLE);
            // Mở ForgetPasswordFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ForgotPasswordFragment()) // fragment_container là ID của View chứa fragment
                    .addToBackStack(null) // Cho phép quay lại màn trước đó
                    .commit();
        });
    }

    private void shakeEditText(EditText editText) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        editText.startAnimation(shake);
    }


    void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    void navigateToSecondActivity() {
        finish();
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    String personName = account.getDisplayName();
                    String personEmail = account.getEmail();

                    // Kiểm tra xem user đã tồn tại chưa
                    UserManager userManager = UserManager.getInstance();
                    User existingUser = userManager.findUserByEmail(personEmail);

                    if (existingUser == null) {
                        // Nếu chưa có, tạo user mới với giá trị mặc định
                        User newUser = new User(
                                personName,                     // name
                                "",                             // password (không cần thiết)
                                personEmail,                    // email
                                "Unknown",                      // sex (giá trị mặc định)
                                20,                           // weight (giá trị ngẫu nhiên)
                                personName,                     // fullname
                                20,                             // age (giá trị mặc định)
                                20,                          // height (giá trị mặc định)
                                "Type 2",                       // diabetesType (giá trị mặc định)
                                "No additional info",           // additionInfo
                                false                           // hasFamilyHistory
                        );

                        // Thêm vào danh sách người dùng
                        userManager.addUser(newUser);
                        existingUser = newUser;
                        Gson gson = new Gson();
                        String userJson = gson.toJson(existingUser);
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("loggedInUser", userJson);
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();

                        navigateToSecondActivity();
                    }

                }
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            findViewById(R.id.scrollView2).setVisibility(View.VISIBLE);
            findViewById(R.id.constraintLayout2).setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }
}