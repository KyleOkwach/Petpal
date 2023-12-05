package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flow.petpal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Settings extends AppCompatActivity {

    private TextView emailTV, usernameTV;
    private ImageView profileIV;
    private ConstraintLayout buttonBack, buttonLogout, buttonEditInformation;
    private LinearLayout buttonTheme;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();

        emailTV = findViewById(R.id.emailTV);
        usernameTV = findViewById(R.id.usernameTV);
        profileIV = findViewById(R.id.profileIV);
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

        showDetails();

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

    private void showDetails() {
        ref.child("Users").child(user.getUid()).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    usernameTV.setText(task.getResult().getValue().toString());
                }
            }
        });

        ref.child("Users").child(user.getUid()).child("imageUri").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Glide.with(Settings.this)
                            .load(task.getResult().getValue().toString())
                            .placeholder(R.drawable.image_broken)
                            .error(R.drawable.image_broken)
                            .into(profileIV);
                }
            }
        });

    }
}