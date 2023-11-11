package com.flow.petpal.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.flow.petpal.R;

public class Login extends AppCompatActivity {
    private ConstraintLayout buttonLogin;
    private ConstraintLayout signUpRedirect;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Login
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Redirect to sign up
        signUpRedirect = findViewById(R.id.signUpRedirect);
        signUpRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect();
            }
        });
    }

    private void login() {
        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
    }

    private void redirect() {
        startActivity(new Intent(Login.this, Signup.class));
        finish();
    }
}