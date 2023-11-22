package com.flow.petpal.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flow.petpal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView emailTV, usernameTV;
    private ConstraintLayout buttonBack, buttonLogout, buttonEditInformation;
    private LinearLayout buttonTheme;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        auth = FirebaseAuth.getInstance();
        emailTV = findViewById(R.id.emailTV);
        buttonBack = findViewById(R.id.buttonBack);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonTheme = findViewById(R.id.buttonTheme);
        buttonEditInformation = findViewById(R.id.buttonEditInformation);

        // Load the theme preference
        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        boolean isNightMode = sharedPreferences.getBoolean("night_mode", false);

        user = auth.getCurrentUser();  // Current logged in user
        if(user == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        } else {
            emailTV.setText(user.getEmail());
        }

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to previous activity
                finish();
            }
        });

        buttonTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply the correct theme
                if (isNightMode) {
                    setTheme(R.style.Base_Theme_Petpal);
                } else {
                    setTheme(R.style.Base_Theme_Petpal);
                }
            }
        });

        buttonEditInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, UserForm.class));
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Splashscreen.class));
                finish();
            }
        });
    }
}