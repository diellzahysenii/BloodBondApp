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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbondapp.R;

public class DonationFragment extends Fragment {

    //private TextView tvUserName, tvUserCity, tvUserBloodGroup, tvUserMobile;
    private Button btnPostDonation;
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

        // Database access
        Database database = new Database(getContext(), "bloodbondapp", null, 1);

        // Get the logged-in user's ID
        int userId = getLoggedInUserId();

        if (userId != -1) {
            populateUserData(database, userId, etUserName, etUserCity, etUserBloodGroup, etUserMobile);
        } else {
            Toast.makeText(getContext(), "No logged-in user found!", Toast.LENGTH_SHORT).show();
        }

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



//    private void loadUserInfo() {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.query("users", null, null, null, null, null, null);
//
//        // Check if cursor has any data
//        if (cursor != null && cursor.moveToFirst()) {
//            // Get column indexes
//            int nameIndex = cursor.getColumnIndex("name");
//            int cityIndex = cursor.getColumnIndex("city");
//            int bloodGroupIndex = cursor.getColumnIndex("blood_group");
//            int mobileIndex = cursor.getColumnIndex("mobile");
//
//            // Check if column indexes are valid
//            if (nameIndex != -1 && cityIndex != -1 && bloodGroupIndex != -1 && mobileIndex != -1) {
//                String name = cursor.getString(nameIndex);
//                String city = cursor.getString(cityIndex);
//                String bloodGroup = cursor.getString(bloodGroupIndex);
//                String mobile = cursor.getString(mobileIndex);
//
//                tvUserName.setText("Name: " + name);
//                tvUserCity.setText("City: " + city);
//                tvUserBloodGroup.setText("Blood Group: " + bloodGroup);
//                tvUserMobile.setText("Mobile: " + mobile);
//            } else {
//
//                Log.e("DonationFragment", "One or more column indices are invalid.");
//            }
//        } else {
//            Log.e("DonationFragment", "No user data found.");
//        }
//
//        cursor.close();
//        db.close();
//    }

//
//    private void postDonation() {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//
//        cv.put("name", tvUserName.getText().toString().replace("Name: ", ""));
//        cv.put("city", tvUserCity.getText().toString().replace("City: ", ""));
//        cv.put("blood_group", tvUserBloodGroup.getText().toString().replace("Blood Group: ", ""));
//        cv.put("mobile", tvUserMobile.getText().toString().replace("Mobile: ", ""));
//
//        db.insert("donations", null, cv);
//        db.close();
//
//
//        Toast.makeText(getContext(), "Donation posted successfully", Toast.LENGTH_SHORT).show();
//    }
}
