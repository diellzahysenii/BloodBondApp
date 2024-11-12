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

import com.example.bloodbondapp.Menu.HomeFragment;
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
                Database db = new Database(getApplicationContext(), "bloodbondapp", null, 1);

                if (password.isEmpty() || mobile.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    if (db.login(mobile, password) == 1) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();  // Finish LoginActivity so it’s removed from the back stack

                        Toast.makeText(LoginActivity.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeFragment.class));
                    } else {
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
