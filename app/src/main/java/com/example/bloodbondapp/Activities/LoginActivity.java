package com.example.bloodbondapp.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.bloodbondapp.R;

public class LoginActivity extends AppCompatActivity {

    private EditText passwordEt, mobileEt;
    private TextView registerTextEt;
    private Button login_buttonEt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mobileEt = findViewById(R.id.mobile_number);
        passwordEt = findViewById(R.id.password);
        registerTextEt = findViewById(R.id.registerText);
        login_buttonEt = findViewById(R.id.login_button);

        login_buttonEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordEt.getText().toString();
                String mobile = mobileEt.getText().toString();
                Database db = new Database(getApplicationContext(), "bloodbondapp", null, 2);

                // Check if the password or mobile fields are empty
                if (password.isEmpty() || mobile.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the login credentials are valid using db.login()
                    if (db.login(mobile, password) == 1) {
                        // If login is successful, retrieve the user ID and save it to SharedPreferences
                        int userId = db.getUserId(mobile, password);
                        getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                                .edit()
                                .putInt("userId", userId)
                                .apply();


                        // Navigate to HomeActivity and clear the current activity stack
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        // Finish the LoginActivity to remove it from the back stack
                        finish();

                        // Show login success message
                        Toast.makeText(LoginActivity.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
                    } else {
                        // If the login fails, show an error message
                        Toast.makeText(LoginActivity.this, "Invalid Number and password", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        registerTextEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
