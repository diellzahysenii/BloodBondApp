package com.example.bloodbondapp.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbondapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DonationFragment extends Fragment {

    //private TextView tvUserName, tvUserCity, tvUserBloodGroup, tvUserMobile;
    private Button btnPostDonation;
    TextView deleteDonationText;
    private Database dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donation, container, false);


        // Bind views
        EditText etUserName = view.findViewById(R.id.etUserName);
        EditText etUserCity = view.findViewById(R.id.etUserCity);
        EditText etUserBloodGroup = view.findViewById(R.id.etUserBloodGroup);
        EditText etUserMobile = view.findViewById(R.id.etUserMobile);
        btnPostDonation = view.findViewById(R.id.btnPostDonation);
        deleteDonationText=view.findViewById(R.id.deleteDonationText);

        // Initialize the dbHelper
        dbHelper = new Database(getContext(), "bloodbondapp", null, 1);

        // Get the logged-in user's ID
        int userId = getLoggedInUserId();

        if (userId != -1) {
            populateUserData(dbHelper, userId, etUserName, etUserCity, etUserBloodGroup, etUserMobile);
        } else {
            Toast.makeText(getContext(), "No logged-in user found!", Toast.LENGTH_SHORT).show();
        }


        btnPostDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String userName = etUserName.getText().toString().trim();
                String userCity = etUserCity.getText().toString().trim();
                String userBloodGroup = etUserBloodGroup.getText().toString().trim();
                String userMobile = etUserMobile.getText().toString().trim();

                // Check if all fields are filled
                if (!userName.isEmpty() && !userCity.isEmpty() && !userBloodGroup.isEmpty() && !userMobile.isEmpty()) {
                    // Get the logged-in user's ID
                    int userId = getLoggedInUserId();

                    if (userId != -1) {
                        // Check for duplicate donation
                        boolean donationExists = dbHelper.checkDuplicateDonation(userId, userName, userCity, userBloodGroup, userMobile);

                        if (donationExists) {
                            Toast.makeText(getContext(), "This donation already exists!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Insert donation data into the database
                            boolean isInserted = dbHelper.insertDonation(userId, userName, userCity, userBloodGroup, userMobile);
                            if (isInserted) {
                                Toast.makeText(getContext(), "Donation posted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to post donation", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "No logged-in user found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteDonationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the logged-in user's ID
                int userId = getLoggedInUserId();

                if (userId != -1) {
                    // Call the deleteDonation method
                    boolean isDeleted = dbHelper.deleteDonation(userId);

                    if (isDeleted) {
                        Toast.makeText(getContext(), "Donation deleted successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Failed to delete donation", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No logged-in user found!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }


    private void populateUserData(Database database, int userId, EditText etUserName, EditText etUserCity, EditText etUserBloodGroup, EditText etUserMobile) {
        Cursor cursor = database.getUserData(userId);
        if (cursor != null && cursor.moveToFirst()) {
            etUserName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            etUserCity.setText(cursor.getString(cursor.getColumnIndexOrThrow("city")));
            etUserBloodGroup.setText(cursor.getString(cursor.getColumnIndexOrThrow("blood_group")));
            etUserMobile.setText(cursor.getString(cursor.getColumnIndexOrThrow("mobile")));
            cursor.close();
        } else {
            Toast.makeText(getContext(), "User data not found!", Toast.LENGTH_SHORT).show();
        }
    }



    private int getLoggedInUserId() {
        return requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                .getInt("userId", -1); // Default to -1 if not found
    }

}
