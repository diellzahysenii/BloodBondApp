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
    private ArrayList<HashMap<String, String>> donationList = new ArrayList<>(); // Store all donations
    private SimpleAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false); // Update the layout name

        // Bind views
        donationListView = view.findViewById(R.id.donationListView);

        // Initialize Database helper
        dbHelper = new Database(getContext(), "bloodbondapp", null, 2);

        // Retrieve and display the donations
        loadDonations();

        return view;
    }
    private void loadDonations() {
        // Show a loading indicator (optional)
        Toast.makeText(getContext(), "Loading donations...", Toast.LENGTH_SHORT).show();

        // Use the asynchronous method to fetch donations
        dbHelper.getAllDonationsAsync(donations -> {
            // Clear the existing donation list
            donationList.clear();

            if (donations != null && !donations.isEmpty()) {
                // Add the fetched donations to the list
                donationList.addAll(donations);

                // Define the columns to be displayed
                String[] from = {"name", "city", "blood_group", "mobile"};
                int[] to = {R.id.tvName, R.id.tvCity, R.id.tvBloodGroup, R.id.tvMobile};

                // Create and set the SimpleAdapter
                adapter = new SimpleAdapter(
                        getContext(),
                        donationList,
                        R.layout.donation_list_item,
                        from,
                        to
                );

                donationListView.setAdapter(adapter); // Set the adapter
            } else {
                Toast.makeText(getContext(), "No donations available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void filterDonations(String query) {
        ArrayList<HashMap<String, String>> filteredList = new ArrayList<>();

        for (HashMap<String, String> donation : donationList) {
            String bloodGroup = donation.get("blood_group");
            if (bloodGroup != null && bloodGroup.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(donation);
            }
        }

        // Update the adapter with the filtered list
        adapter = new SimpleAdapter(
                getContext(),
                filteredList,
                R.layout.donation_list_item,
                new String[]{"name", "city", "blood_group", "mobile"},
                new int[]{R.id.tvName, R.id.tvCity, R.id.tvBloodGroup, R.id.tvMobile}
        );

        donationListView.setAdapter(adapter);
    }



}
