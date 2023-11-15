package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flow.petpal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {
    private EditText usernameSignup, emailSignup, passwordSignup, passwordConfirm;
    private ConstraintLayout buttonShowPass, buttonSignUp, loginRedirect;
    private ImageView visibilityView;
    private TextView buttonText;
    private ProgressBar loadSignUp;
    private boolean passVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Form widgets init
        usernameSignup = findViewById(R.id.usernameSignup);
        emailSignup = findViewById(R.id.emailSignup);
        passwordSignup = findViewById(R.id.passwordSignup);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonText = findViewById(R.id.buttonText);
        loadSignUp = findViewById(R.id.loadSignUp);

        loadSignUp.setVisibility(View.INVISIBLE);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSignUp.setVisibility(View.VISIBLE);
                buttonText.setVisibility(View.INVISIBLE);
                String username, email, pass, passConfirm;

                username = String.valueOf(usernameSignup.getText());
                email = String.valueOf(emailSignup.getText());
                pass = String.valueOf(passwordSignup.getText());
                passConfirm = String.valueOf(passwordConfirm.getText());

                // Check if all fields have values
                if(TextUtils.isEmpty(username)) {
                    // No username is given
                    Toast.makeText(Signup.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(email)) {
                    // No email is given
                    Toast.makeText(Signup.this, "Please enter an email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass)) {
                    // No password is given
                    Toast.makeText(Signup.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(passConfirm)) {
                    // No confirmation password is given
                    Toast.makeText(Signup.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                } else {
                    // All input is valid
                    if(!pass.equals(passConfirm)) {
                        // Passwords are different
                        Toast.makeText(Signup.this, "The passwords don't match", Toast.LENGTH_SHORT).show();
                    } else {
                        signUp(username, email, pass);
                    }
                }
                loadSignUp.setVisibility(View.INVISIBLE);
                buttonText.setVisibility(View.VISIBLE);
            }
        });

        // Show password
        buttonShowPass = findViewById(R.id.buttonShowPassword);
        visibilityView = findViewById(R.id.visibilityView);
        buttonShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passVisible) {
                    // If password is visible
                    // Change icon
                    // visibilityView.setImageDrawable();

                    // Change TextView type
                    passwordSignup.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordSignup.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } passVisible = !passVisible;
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

    private void signUp(String username, String email, String password) {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Signup successful",
                                    Toast.LENGTH_SHORT).show();

                            // Move to next activity
                            startActivity(new Intent(Signup.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Signup.this, task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void redirect() {
        startActivity(new Intent(Signup.this, Login.class));
        finish();
    }
}