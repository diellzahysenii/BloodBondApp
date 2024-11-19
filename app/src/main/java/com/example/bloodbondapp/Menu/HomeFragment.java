package com.example.bloodbondapp.Menu;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bloodbondapp.R;
import com.example.bloodbondapp.Activities.Database;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    private ListView donationListView;
    private Database dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false); // Update the layout name

        // Bind views
        donationListView = view.findViewById(R.id.donationListView);

        // Initialize Database helper
        dbHelper = new Database(getContext(), "bloodbondapp", null, 1);

        // Retrieve and display the donations
        loadDonations();

        return view;
    }
    private String getColumnString(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex != -1) {
            return cursor.getString(columnIndex);
        } else {
            // If column doesn't exist, log the error and return a default value
            Log.e("HomeFragment", "Column '" + columnName + "' not found in the database.");
            return "N/A"; // You can return a default value here
        }
    }


    private void loadDonations() {
        // Query the database to retrieve all donations
        Cursor cursor = dbHelper.getAllDonations();  // Make sure this method is implemented in your Database class

        // Create a list to store donation data
        ArrayList<HashMap<String, String>> donationList = new ArrayList<>();

        // Check if cursor has data
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Create a HashMap to store each donation's details
                HashMap<String, String> donation = new HashMap<>();
                donation.put("name", getColumnString(cursor, "name"));
                donation.put("city", getColumnString(cursor, "city"));
                donation.put("blood_group", getColumnString(cursor, "blood_group"));
                donation.put("mobile", getColumnString(cursor, "mobile"));

                donationList.add(donation);  // Add the HashMap to the list
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();

            // Define the columns to be displayed
            String[] from = {"name", "city", "blood_group", "mobile"};
            int[] to = {R.id.tvName, R.id.tvCity, R.id.tvBloodGroup, R.id.tvMobile};

            // Create a SimpleAdapter to bind the data to the ListView
            SimpleAdapter adapter = new SimpleAdapter(
                    getContext(),
                    donationList,
                    R.layout.donation_list_item,  // Create a custom layout for each donation item
                    from,
                    to
            );

            donationListView.setAdapter(adapter);  // Set the adapter to the ListView
        } else {
            Toast.makeText(getContext(), "No donations available", Toast.LENGTH_SHORT).show();
        }
    }
}
