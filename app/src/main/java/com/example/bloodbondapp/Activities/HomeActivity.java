package com.example.bloodbondapp.Activities;

import androidx.activity.EdgeToEdge;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.bloodbondapp.Menu.AboutFragment;
import com.example.bloodbondapp.Menu.HomeFragment;
import com.example.bloodbondapp.R;
import com.google.android.material.navigation.NavigationView;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    //e kum kqyr qet tutorial https://www.youtube.com/watch?v=6mgTJdy_di4
    // kshtu po bon mi qel faqet ama kur pi preki tri vijat per me mshel po bon Crash App-i
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.drawer);
        if (drawerLayout == null) {
            Log.e("HomeActivity", "DrawerLayout is null");
        }
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }


        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        if(savedInstanceState==null){
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_home);
//        }

        if (savedInstanceState == null) {
            Fragment donationFragment = new DonationFragment(); // Choose either DonationFragment or HomeFragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, donationFragment).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Check if the drawer is open
                if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    // If the drawer is open, close it
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish(); // Close the activity or navigate back
                }
            }
        });
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return true;
        } else if (item.getItemId() == R.id.nav_donate) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DonationFragment()).commit();
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return true;
        } else if (item.getItemId() == R.id.nav_request) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RequestFragment()).commit();
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return true;
        } else if (item.getItemId() == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return true;
        } else if (item.getItemId() == R.id.nav_logout) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return true;
        }else{
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        return true;
        }
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.nav_home:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
//                break;
//            case R.id.nav_donate:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DonationFragment()).commit();
//                break;
//            case R.id.nav_request:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RequestFragment()).commit();
//                break;
//            case R.id.nav_about:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
//                break;
//            case R.id.nav_logout:
//                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
//                break;
//        }
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }


//    @Override
//    public void onBackPressed() {
//        // Check if the drawer is open
//        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            // If the drawer is open, close it
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            // If the drawer is not open, proceed with the default back press behavior
//            super.onBackPressed();
//        }
//    }
}