package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.flow.petpal.Adapters.PetAdapter;
import com.flow.petpal.Models.PetModel;
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

import java.util.ArrayList;

public class PetDialog extends AppCompatActivity {

    private ConstraintLayout buttonCancel;
    private TextView modeTV;
    private String dialogMode;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private DatabaseReference petsRef;

    // Firebase storage
    private FirebaseStorage storage;
    private StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_dialog);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        user = auth.getCurrentUser();

        dialogMode = getIntent().getStringExtra("mode");

        buttonCancel = findViewById(R.id.buttonCancel);
        modeTV = findViewById(R.id.modeTV);

        modeTV.setText("Pet "+dialogMode);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        GridView petGV;

        petGV = findViewById(R.id.idGVpets);
        ArrayList<PetModel> petModelArrayList = new ArrayList<PetModel>();

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors while fetching data
                Log.e("FirebaseError", "Error fetching pets: " + databaseError.getMessage());
            }
        });
    }
}