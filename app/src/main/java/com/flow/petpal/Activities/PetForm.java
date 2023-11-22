package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.flow.petpal.Models.PetModel;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class PetForm extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1000;
    private ConstraintLayout buttonUploadImage, buttonAddPet;
    private EditText petNameET, petDOBET, petSpeciesET, petGenderET;
    private ImageView petIV;
    private Uri storedImageURI;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference ref;

    // Firebase storage
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_form);

        buttonUploadImage = findViewById(R.id.buttonUploadImage);
        buttonAddPet = findViewById(R.id.buttonAddPet);
        petNameET = findViewById(R.id.petName);
        petDOBET = findViewById(R.id.petDOB);
        petSpeciesET = findViewById(R.id.petSpecies);
        petGenderET = findViewById(R.id.petGender);
        petIV = findViewById(R.id.petIV);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        user = auth.getCurrentUser();  // Current logged in user
        if(user == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        buttonAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String petID, petName, ownerID, petDOB, petSpecies, petGender, imageName;
                petID = UUID.randomUUID().toString();
                ownerID = user.getUid();

                petName = petNameET.getText().toString();
                petDOB = petDOBET.getText().toString();
                petSpecies = petSpeciesET.getText().toString();
                petGender = petGenderET.getText().toString();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
                Date now = new Date();
                imageName = UUID.randomUUID().toString();
                uploadInformation(petID, petName, ownerID, imageName, petDOB, petSpecies, petGender);
            }
        });
    }

    private void uploadInformation(String petID, String petName, String ownerID, String imageName, String petDOB, String petSpecies, String petGender) {
        storageRef = storage.getReference("images/pets/"+imageName);
        storageRef.putFile(storedImageURI)
                .addOnFailureListener(new OnFailureListener() {
                    // Register observers to listen for when the download is done or if it fails
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(PetForm.this, "Error uploading image" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(PetForm.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                                // UserModel user = new UserModel(uid, fname, lname, username, uri.toString());
                                // public PetModel(String petID, String pet_name, String ownerID, String petImageUri, String petDOB, String petSpecies, String petGender) {
                                PetModel pet = new PetModel(petID, petName, ownerID, uri.toString(), petDOB, petSpecies, petGender);
                                ref = db.getReference("Users");
                                ref.child(ownerID+"/Pets/"+petID).setValue(pet).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(PetForm.this, "Pet added successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {

            if(requestCode == PICK_IMAGE_REQUEST) {
                // Set imageView to selected image URI
                storedImageURI = data.getData();
                petIV.setImageURI(storedImageURI);
            }
        }
    }
}