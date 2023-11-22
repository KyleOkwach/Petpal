package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.flow.petpal.Models.UserModel;
import com.flow.petpal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class UserForm extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1000;
    private ConstraintLayout buttonCancelForm, buttonSelectImage, buttonProceed;
    private EditText usernameTV, fnameTV, lnameTV;
    private ImageView userIV;
    private Uri storedImageURI;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference ref;

    // Firebase storage
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private UploadTask uploadTask;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);

        buttonCancelForm = findViewById(R.id.buttonCancelForm);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonProceed = findViewById(R.id.buttonProceed);
        usernameTV = findViewById(R.id.userTV);
        fnameTV = findViewById(R.id.fnameTV);
        lnameTV = findViewById(R.id.lnameTV);
        userIV = findViewById(R.id.userIV);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        user = auth.getCurrentUser();  // Current logged in user
        if(user == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }

        buttonCancelForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, fname, lname, uid, imageName, time;
                fname = String.valueOf(fnameTV.getText());
                lname = String.valueOf(lnameTV.getText());
                username = String.valueOf(usernameTV.getText());
                uid = user.getUid();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
                Date now = new Date();
                imageName = UUID.randomUUID().toString();

                if (TextUtils.isEmpty(username)) {
                    // No email is given
                    Toast.makeText(UserForm.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                } else {
                    uploadInformation(uid, username, fname, lname, imageName);
                }
            }
        });
    }

    private void uploadInformation(String uid, String username, String fname, String lname, String imageName) {
        storageRef = storage.getReference("images/"+imageName);
        // Toast.makeText(UserForm.this, storageRef.getPath(), Toast.LENGTH_SHORT).show();
        Log.d("StorageRefPath", storageRef.getPath());
        storageRef.putFile(storedImageURI)
            .addOnFailureListener(new OnFailureListener() {
                // Register observers to listen for when the download is done or if it fails
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(UserForm.this, "Error uploading image" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UserForm.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                            UserModel user = new UserModel(uid, fname, lname, username, uri.toString());
                            ref = db.getReference("Users");
                            ref.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(UserForm.this, "Information successfully updated", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    });
                }
            });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image from here..."),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {

            if(requestCode == PICK_IMAGE_REQUEST) {
                // Set imageView to selected image URI
                storedImageURI = data.getData();
                userIV.setImageURI(storedImageURI);
            }
        }
    }
}