package com.example.bloodbondapp.Activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbondapp.R;

public class DonationFragment extends Fragment {

    private TextView tvUserName, tvUserCity, tvUserBloodGroup, tvUserMobile;
    private Button btnPostDonation;
    private Database dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donation, container, false);

        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserCity = view.findViewById(R.id.tvUserCity);
        tvUserBloodGroup = view.findViewById(R.id.tvUserBloodGroup);
        tvUserMobile = view.findViewById(R.id.tvUserMobile);
        btnPostDonation = view.findViewById(R.id.btnPostDonation);

        dbHelper = new Database(getContext(), "bloodbond.db", null, 1);

        // Load user information
        loadUserInfo();

        // Set click listener for Post Donation button
        btnPostDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDonation();
            }
        });

        return view;
    }

    private void loadUserInfo() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, null, null, null, null, null);

        // Check if cursor has any data
        if (cursor != null && cursor.moveToFirst()) {
            // Get column indexes
            int nameIndex = cursor.getColumnIndex("name");
            int cityIndex = cursor.getColumnIndex("city");
            int bloodGroupIndex = cursor.getColumnIndex("blood_group");
            int mobileIndex = cursor.getColumnIndex("mobile");

            // Check if column indexes are valid
            if (nameIndex != -1 && cityIndex != -1 && bloodGroupIndex != -1 && mobileIndex != -1) {
                String name = cursor.getString(nameIndex);
                String city = cursor.getString(cityIndex);
                String bloodGroup = cursor.getString(bloodGroupIndex);
                String mobile = cursor.getString(mobileIndex);

                tvUserName.setText("Name: " + name);
                tvUserCity.setText("City: " + city);
                tvUserBloodGroup.setText("Blood Group: " + bloodGroup);
                tvUserMobile.setText("Mobile: " + mobile);
            } else {

                Log.e("DonationFragment", "One or more column indices are invalid.");
            }
        } else {
            Log.e("DonationFragment", "No user data found.");
        }

        cursor.close();
        db.close();
    }


    private void postDonation() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", tvUserName.getText().toString().replace("Name: ", ""));
        cv.put("city", tvUserCity.getText().toString().replace("City: ", ""));
        cv.put("blood_group", tvUserBloodGroup.getText().toString().replace("Blood Group: ", ""));
        cv.put("mobile", tvUserMobile.getText().toString().replace("Mobile: ", ""));

        db.insert("donations", null, cv);
        db.close();


        Toast.makeText(getContext(), "Donation posted successfully", Toast.LENGTH_SHORT).show();
    }
}
