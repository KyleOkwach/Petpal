package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flow.petpal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {
    private EditText emailLogin, passwordLogin;
    private ConstraintLayout buttonLogin;
    private ConstraintLayout signUpRedirect;
    private TextView buttonText;
    private ProgressBar loadLogin;
    private boolean loggingIn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Widgets
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonText = findViewById(R.id.buttonText);
        loadLogin = findViewById(R.id.loadLogin);

        loadLogin.setVisibility(View.INVISIBLE);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loggingIn) {
                    loadLogin.setVisibility(View.VISIBLE);
                    buttonText.setVisibility(View.INVISIBLE);
                    loggingIn = true;
                    String email, pass;

                    email = String.valueOf(emailLogin.getText());
                    pass = String.valueOf(passwordLogin.getText());

                    buttonLogin.setBackgroundResource(R.drawable.button_disabled);

                    if (TextUtils.isEmpty(email)) {
                        // No email/username is given
                        Toast.makeText(Login.this, "Please enter an email", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(pass)) {
                        // No password is given
                        Toast.makeText(Login.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                    } else {
                        // All input is valid
                        login(email, pass);
                    }

                    loadLogin.setVisibility(View.INVISIBLE);
                    buttonText.setVisibility(View.VISIBLE);
                    loggingIn = false;
                }
                buttonLogin.setBackgroundResource(R.drawable.button_basic);
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

    private void login(String email, String password) {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login successful",
                                    Toast.LENGTH_SHORT).show();

                            //Move to next activity
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void redirect() {
        startActivity(new Intent(Login.this, Signup.class));
        finish();
    }
}