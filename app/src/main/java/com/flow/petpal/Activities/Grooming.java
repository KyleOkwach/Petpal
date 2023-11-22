package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.flow.petpal.Adapters.EventAdapter;
import com.flow.petpal.Models.EventModel;
import com.flow.petpal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class Grooming extends AppCompatActivity {
    private ConstraintLayout buttonBack, buttonSetDate;
    private EditText activityDateET, activityDescriptionET;
    private ProgressBar submitting;
    private RecyclerView activitiesRV;
    private String petID;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private  DatabaseReference groomingRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grooming);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();
        petID = getIntent().getStringExtra("PetID");

        activityDateET = findViewById(R.id.date);
        activityDescriptionET = findViewById(R.id.description);
        submitting = findViewById(R.id.submitting);
        buttonSetDate = findViewById(R.id.buttonSetDate);
        buttonBack = findViewById(R.id.buttonBack);
        activitiesRV = findViewById(R.id.activitiesRV);

        submitting.setVisibility(View.INVISIBLE);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groomingID, groomingDate, groomingDescription;
                groomingID = UUID.randomUUID().toString();
                groomingDate = activityDateET.getText().toString();
                groomingDescription = activityDescriptionET.getText().toString();
                setGroomingDate(groomingID, groomingDate, groomingDescription, petID);
            }
        });

        // Populate groomings list
        showGroomingSchedule();
    }

    private void setGroomingDate(String groomingID, String groomingDate, String groomingDescription, String petID) {
        submitting.setVisibility(View.VISIBLE);

        //
        // PetModel pet = new PetModel(petID, petName, ownerID, uri.toString(), petDOB, petSpecies, petGender);
        EventModel grooming = new EventModel(groomingID, groomingDate, groomingDescription, petID);
        ref = db.getReference("Users");
        ref.child(user.getUid()+"/Pets/"+petID+"/Grooming/"+groomingID).setValue(grooming).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Grooming.this, "grooming date set successfully", Toast.LENGTH_SHORT).show();
                showGroomingSchedule();
                activityDateET.setText("");
                activityDescriptionET.setText("");
            }
        });

        submitting.setVisibility(View.INVISIBLE);
    }

    private void showGroomingSchedule() {
        groomingRef = db.getReference()
                .child("Users")
                .child(user.getUid())
                .child("Pets")
                .child(petID)
                .child("Grooming");
        groomingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<EventModel> eventModelArrayList = new ArrayList<EventModel>();

                for (DataSnapshot petSnapshot : dataSnapshot.getChildren()) {
                    // Access each pet item
                    String groomingID = petSnapshot.getKey();
                    String groomingDate = petSnapshot.child("eventDate").getValue(String.class);
                    String groomingDescription = petSnapshot.child("eventDescription").getValue(String.class);

                    eventModelArrayList.add(new EventModel(groomingID, groomingDate, groomingDescription, petID));
                }

                EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), eventModelArrayList);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                // in below two lines we are setting layoutmanager and adapter to our recycler view.
                activitiesRV.setLayoutManager(linearLayoutManager);
                activitiesRV.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors while fetching data
                Log.e("FirebaseError", "Error fetching pets: " + databaseError.getMessage());
            }
        });
    }
}