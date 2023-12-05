package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.View;
import android.widget.GridView;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.flow.petpal.Adapters.PetAdapter;
import com.flow.petpal.Models.PetModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.flow.petpal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Pets extends AppCompatActivity {

    private ConstraintLayout buttonAddPet;
    private ProgressBar progressBar;
    private GridView petGV;

    private FirebaseAuth auth;  // User authentication object
    private FirebaseUser user;  // User data object
    private FirebaseDatabase db;  // Database objects
    private DatabaseReference ref;
    private DatabaseReference petsRef;  // Database reference

    // Firebase storage
    private FirebaseStorage storage;  // Instance DB file storage
    private StorageReference storageRef;  // Reference of DB file storage
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        buttonAddPet = findViewById(R.id.buttonAddPet);
        petGV = findViewById(R.id.idGVpets);
        progressBar = findViewById(R.id.progressBarPets);
        progressBar.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();  // Instance of database
        storage = FirebaseStorage.getInstance();
        user = auth.getCurrentUser();  // Current logged in user

        // Retrieve pets from database
        showPets();

        buttonAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pets.this, PetForm.class));
            }
        });

        // ---------------------
        // | BOTTOM NAVIGATION |
        // ---------------------

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.pets);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.pets:
                        return true;
                    case R.id.community:
                        startActivity(new Intent(getApplicationContext(),Community.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void showPets() {
        progressBar.setVisibility(View.VISIBLE);
        petsRef = db.getReference().child("Users").child(user.getUid()).child("Pets");
        petsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<PetModel> petModelArrayList = new ArrayList<PetModel>();

                for (DataSnapshot petSnapshot : dataSnapshot.getChildren()) {
                    // Access each pet item
                    String petID = petSnapshot.getKey(); // Get the unique ID of the pet
                    String petName = petSnapshot.child("pet_name").getValue(String.class); // Get the name of the pet
                    String petDob = petSnapshot.child("petDOB").getValue(String.class);
                    String petSpecies = petSnapshot.child("petSpecies").getValue(String.class);
                    String petImage = petSnapshot.child("petImageUri").getValue(String.class);
                    String petGender = petSnapshot.child("petGender").getValue(String.class);

                    petModelArrayList.add(new PetModel(petID, petName, user.getUid().toString(), petImage, petDob, petSpecies, petGender));
                }

                PetAdapter adapter = new PetAdapter(getApplicationContext(), petModelArrayList);
                petGV.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors while fetching data
                Log.e("FirebaseError", "Error fetching pets: " + databaseError.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}