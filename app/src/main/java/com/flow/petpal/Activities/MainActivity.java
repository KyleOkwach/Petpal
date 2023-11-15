package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.flow.petpal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView welcome;
    private ConstraintLayout buttonSettings, buttonHealth, buttonNutrition, buttonGrooming, buttonTraining;
    private String dialogMode;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        welcome = findViewById(R.id.welcomeText);
        buttonSettings = findViewById(R.id.buttonSettings);
        buttonHealth = findViewById(R.id.buttonHealth);
        buttonNutrition = findViewById(R.id.buttonNutrition);
        buttonGrooming = findViewById(R.id.buttonGrooming);
        buttonTraining = findViewById(R.id.buttonTraining);

        user = auth.getCurrentUser();  // Current logged in user
        if(user == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        } else {
            welcome.setText("Welcome " + user.getEmail());
        }

        // ----------------------
        // | PRIMARY NAVIGATION |
        // ----------------------

        buttonHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMode = "Health";
                startActivity(new Intent(MainActivity.this, PetDialog.class).putExtra("mode", dialogMode));
            }
        });

        buttonNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMode = "Nutrition";
                startActivity(new Intent(MainActivity.this, PetDialog.class).putExtra("mode", dialogMode));
            }
        });

        buttonGrooming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMode = "Grooming";
                startActivity(new Intent(MainActivity.this, PetDialog.class).putExtra("mode", dialogMode));
            }
        });

        buttonTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMode = "Training";
                startActivity(new Intent(MainActivity.this, PetDialog.class).putExtra("mode", dialogMode));
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Settings.class));
            }
        });


        // ---------------------
        // | BOTTOM NAVIGATION |
        // ---------------------

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.pets:
                        startActivity(new Intent(getApplicationContext(),Pets.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.community:
                        startActivity(new Intent(getApplicationContext(),Community.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });

    }
}