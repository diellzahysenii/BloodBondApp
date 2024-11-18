
package com.example.bloodbondapp.Activities;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.bloodbondapp.Menu.AboutFragment;
import com.example.bloodbondapp.Menu.HomeFragment;
import com.example.bloodbondapp.R;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Comment this if unnecessary
        setContentView(R.layout.activity_home);

        // Initialize the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the DrawerLayout
        drawerLayout = findViewById(R.id.drawer);
        if (drawerLayout == null) {
            Log.e("HomeActivity", "DrawerLayout is null");
        }

        // Setup ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Setup NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Access the header view in NavigationView
        View headerView = navigationView.getHeaderView(0);  // 0 because it's the first header

        // Find the TextView in the header layout
        TextView userNameTextView = headerView.findViewById(R.id.userName);

        // Retrieve the userId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1); // Retrieve the userId

        if (userId != -1) {
            // Get user data from the database
            Database db = new Database(this, "bloodbondapp", null, 1);
            Cursor cursor = db.getUserData(userId);

            // Check if cursor is not null and move to the first record
            if (cursor != null && cursor.moveToFirst()) {
                // Get the column index for "name" (or any other column you want)
                int nameColumnIndex = cursor.getColumnIndex("name");

                if (nameColumnIndex != -1) {
                    // If the column exists, retrieve the user's name
                    String userName = cursor.getString(nameColumnIndex);

                    // Set the user's name in the TextView
                    if (userNameTextView != null) {
                        userNameTextView.setText(userName);  // Dynamically set the name in the TextView
                    }
                } else {
                    Log.e("HomeActivity", "Column 'name' not found in the database");
                }

                cursor.close();
            }
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())  // Replace with HomeFragment
                    .commit();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (itemId == R.id.nav_donate) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DonationFragment()).commit();
        } else if (itemId == R.id.nav_request) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RequestFragment()).commit();
        } else if (itemId == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
        } else if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        } else {
            return false;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }


}
