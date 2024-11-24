package com.example.bloodbondapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bloodbondapp.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEt, cityEt, bloodGroupEt, passwordEt, mobileEt;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nameEt = findViewById(R.id.name);
        cityEt = findViewById(R.id.city);
        bloodGroupEt = findViewById(R.id.bloodtype);
        passwordEt = findViewById(R.id.password);
        mobileEt = findViewById(R.id.number);
        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, city, blood_group, password, mobile;
                name = nameEt.getText().toString();
                city = cityEt.getText().toString();
                blood_group = bloodGroupEt.getText().toString();
                password = passwordEt.getText().toString();
                mobile = mobileEt.getText().toString();
                Database db =new Database(getApplicationContext(),"bloodbondapp",null,2);

                if (name.isEmpty() || city.isEmpty() || blood_group.isEmpty() || password.isEmpty() || mobile.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    if (!isValidBloodType(blood_group)) {
                        Toast.makeText(RegisterActivity.this, "Invalid blood type.\nValid types: A+, A-, B+, B-, AB+, AB-, O+, O-", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (!isValidPhoneNumber(mobile)) {
                        Toast.makeText(RegisterActivity.this, "Invalid phone number.\nMust be a 9-digit number.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (!isValidPassword(password)) {
                        Toast.makeText(RegisterActivity.this, "Invalid password.\nPassword must be at least 5 characters long.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    db.register(name,city,blood_group,mobile,password);
                    Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

                }
            }
        });

    }
    public static boolean isValidBloodType(String bloodType) {
        if (bloodType == null) return false;

        String[] validBloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        for (String type : validBloodTypes) {
            if (bloodType.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\\d{9}");
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 5;
    }
}