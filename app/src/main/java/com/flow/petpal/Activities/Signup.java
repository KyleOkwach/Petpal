package com.flow.petpal.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.flow.petpal.R;

public class Signup extends AppCompatActivity {
    private ConstraintLayout buttonSignUp;
    private ConstraintLayout loginRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        // Redirect to sign up
        loginRedirect = findViewById(R.id.loginRedirect);
        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect();
            }
        });
    }

    private void signUp() {
        startActivity(new Intent(Signup.this, MainActivity.class));
        finish();
    }

    private void redirect() {
        startActivity(new Intent(Signup.this, Login.class));
        finish();
    }
}