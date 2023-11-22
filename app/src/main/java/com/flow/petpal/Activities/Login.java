package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


public class Login extends AppCompatActivity {
    private EditText emailLogin, passwordLogin;
    private ConstraintLayout buttonLogin, buttonGoogleAuth;
    private ConstraintLayout signUpRedirect;
    private TextView buttonText;
    private ProgressBar loadLogin;
    private boolean loggingIn;
    private int RC_SIGN_IN = 20;

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    GoogleSignInClient googleSignInClient;

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
        buttonGoogleAuth = findViewById(R.id.buttonGoogleAuth);
        loadLogin = findViewById(R.id.loadLogin);

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