package com.example.bloodbondapp.Activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbondapp.R;

public class RequestFragment extends Fragment {

    private Button btnPostRequest;
    private TextView deleteRequestText;
    private Spinner spinnerBloodGroup;
    private Database dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        // Bind views
        EditText etRequesterName = view.findViewById(R.id.etRequesterName);
        EditText etRequesterCity = view.findViewById(R.id.etRequesterCity);
        spinnerBloodGroup = view.findViewById(R.id.spinnerBloodGroup);
        EditText etRequesterMobile = view.findViewById(R.id.etRequesterMobile);
        btnPostRequest = view.findViewById(R.id.btnPostRequest);
        deleteRequestText = view.findViewById(R.id.deleteRequestText);

        // Initialize the dbHelper
        dbHelper = new Database(getContext(), "bloodbondapp", null, 2);

        // Populate spinner with blood group options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.blood_group_array, // Ensure this array is defined in res/values/strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodGroup.setAdapter(adapter);

        // Get the logged-in user's ID
        int userId = getLoggedInUserId();

        if (userId != -1) {
            populateRequestData(dbHelper, userId, etRequesterName, etRequesterCity, spinnerBloodGroup, etRequesterMobile);
        } else {
            Toast.makeText(getContext(), "No logged-in user found!", Toast.LENGTH_SHORT).show();
        }

        btnPostRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String requesterName = etRequesterName.getText().toString().trim();
                String requesterCity = etRequesterCity.getText().toString().trim();
                String bloodGroupNeeded = spinnerBloodGroup.getSelectedItem().toString();
                String requesterMobile = etRequesterMobile.getText().toString().trim();

                // Check if all fields are filled
                if (!requesterName.isEmpty() && !requesterCity.isEmpty() && !bloodGroupNeeded.isEmpty() && !requesterMobile.isEmpty()) {
                    // Check for duplicate request
                    boolean requestExists = dbHelper.checkDuplicateRequest(userId, requesterName, requesterCity, bloodGroupNeeded, requesterMobile);

                    if (requestExists) {
                        Toast.makeText(getContext(), "This request already exists!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Insert request data into the database
                        boolean isInserted = dbHelper.insertRequest(userId, requesterName, requesterCity, bloodGroupNeeded, requesterMobile);
                        if (isInserted) {
                            Toast.makeText(getContext(), "Request posted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to post request", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteRequestText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the logged-in user's ID
                int userId = getLoggedInUserId();

                if (userId != -1) {
                    // Call the deleteRequest method
                    boolean isDeleted = dbHelper.deleteRequest(userId);

                    if (isDeleted) {
                        Toast.makeText(getContext(), "Request deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to delete request", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No logged-in user found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void populateRequestData(Database database, int userId, EditText etRequesterName, EditText etRequesterCity, Spinner spinnerBloodGroup, EditText etRequesterMobile) {
        Cursor cursor = database.getRequestData(userId);
        if (cursor != null && cursor.moveToFirst()) {
            etRequesterName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            etRequesterCity.setText(cursor.getString(cursor.getColumnIndexOrThrow("city")));
            etRequesterMobile.setText(cursor.getString(cursor.getColumnIndexOrThrow("mobile")));

            // Set the spinner value to match the saved blood group
            String bloodGroup = cursor.getString(cursor.getColumnIndexOrThrow("blood_group"));
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerBloodGroup.getAdapter();
            int position = adapter.getPosition(bloodGroup);
            spinnerBloodGroup.setSelection(position);

            cursor.close();
        } else {
            Toast.makeText(getContext(), "Request data not found!", Toast.LENGTH_SHORT).show();
        }
    }

    private int getLoggedInUserId() {
        return requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                .getInt("userId", -1); // Default to -1 if not found
    }
}
