package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Signup extends AppCompatActivity {
    private EditText emailSignup, passwordSignup, passwordConfirm;
    private ConstraintLayout buttonShowPass, buttonSignUp, loginRedirect, buttonGoogleAuth;
    private ImageView visibilityView;
    private TextView buttonText;
    private ProgressBar loadSignUp;
    private boolean passVisible = false;
    private int RC_SIGN_IN = 20;

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Form widgets init
        emailSignup = findViewById(R.id.emailSignup);
        passwordSignup = findViewById(R.id.passwordSignup);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonText = findViewById(R.id.buttonText);
        buttonGoogleAuth = findViewById(R.id.buttonGoogleAuth);
        loadSignUp = findViewById(R.id.loadSignUp);

        loadSignUp.setVisibility(View.INVISIBLE);

        // GOOGLE AUTH
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // .requestIdToken(getString(R.string.default_web_client_id))
                .requestIdToken("359412879965-oe41cdkraafu3j8ba6l700a8t3i48k7f.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        buttonGoogleAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSignUp.setVisibility(View.VISIBLE);
                buttonText.setVisibility(View.INVISIBLE);
                String username, email, pass, passConfirm;

                email = String.valueOf(emailSignup.getText());
                pass = String.valueOf(passwordSignup.getText());
                passConfirm = String.valueOf(passwordConfirm.getText());

                // Check if all fields have values
                if (TextUtils.isEmpty(email)) {
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
                        signUp(email, pass);
                    }
                }
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
                    passwordConfirm.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordSignup.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordConfirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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

    private void googleSignIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (Exception e) {
                Toast.makeText(this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("uid", user.getUid());
                        map.put("firtName", null);
                        map.put("lastName", null);
                        map.put("userName", user.getDisplayName());
                        map.put("imageUri", user.getPhotoUrl().toString());

                        db.getReference().child("Users").child(user.getUid()).setValue(map);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error sending to server", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void signUp(String email, String password) {
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
                            startActivity(new Intent(Signup.this, UserForm.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Signup.this, task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        loadSignUp.setVisibility(View.INVISIBLE);
                        buttonText.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void redirect() {
        startActivity(new Intent(Signup.this, Login.class));
        finish();
    }
}