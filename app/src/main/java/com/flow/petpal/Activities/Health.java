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

public class Health extends AppCompatActivity {

    private ConstraintLayout buttonBack, buttonAddCheckup;
    private EditText checkupDateET, checkupDescriptionET;
    private ProgressBar submittingCheckup;
    private RecyclerView checkupsRV;
    private String petID;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private  DatabaseReference checkupRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();
        petID = getIntent().getStringExtra("PetID");

        checkupDateET = findViewById(R.id.checkupDate);
        checkupDescriptionET = findViewById(R.id.checkupDescription);
        submittingCheckup = findViewById(R.id.submittingCheckup);
        buttonAddCheckup = findViewById(R.id.buttonAddCheckup);
        buttonBack = findViewById(R.id.buttonBack);
        checkupsRV = findViewById(R.id.checkupRV);

        submittingCheckup.setVisibility(View.INVISIBLE);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonAddCheckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkupID, checkupDate, checkupDescription;
                checkupID = UUID.randomUUID().toString();
                checkupDate = checkupDateET.getText().toString();
                checkupDescription = checkupDescriptionET.getText().toString();
                setCheckupDate(checkupID, checkupDate, checkupDescription, petID);
            }
        });

        // Populate checkups list
        showCheckups();
    }

    private void setCheckupDate(String checkupID, String checkupDate, String checkupDescription, String petID) {
        submittingCheckup.setVisibility(View.VISIBLE);

        //
        // PetModel pet = new PetModel(petID, petName, ownerID, uri.toString(), petDOB, petSpecies, petGender);
        EventModel checkup = new EventModel(checkupID, checkupDate, checkupDescription, petID);
        ref = db.getReference("Users");
        ref.child(user.getUid()+"/Pets/"+petID+"/Checkups/"+checkupID).setValue(checkup).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Health.this, "Checkup date set successfully", Toast.LENGTH_SHORT).show();
                showCheckups();
                checkupDateET.setText("");
                checkupDescriptionET.setText("");
            }
        });

        submittingCheckup.setVisibility(View.INVISIBLE);
    }

    private void showCheckups() {
        checkupRef = db.getReference()
                .child("Users")
                .child(user.getUid())
                .child("Pets")
                .child(petID)
                .child("Checkups");
        checkupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<EventModel> eventModelArrayList = new ArrayList<EventModel>();

                for (DataSnapshot petSnapshot : dataSnapshot.getChildren()) {
                    // Access each pet item
                    String checkupID = petSnapshot.getKey();
                    String checkupDate = petSnapshot.child("eventDate").getValue(String.class);
                    String checkupDescription = petSnapshot.child("eventDescription").getValue(String.class);

                    eventModelArrayList.add(new EventModel(checkupID, checkupDate, checkupDescription, petID));
                }

                EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), eventModelArrayList);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                // in below two lines we are setting layoutmanager and adapter to our recycler view.
                checkupsRV.setLayoutManager(linearLayoutManager);
                checkupsRV.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors while fetching data
                Log.e("FirebaseError", "Error fetching pets: " + databaseError.getMessage());
            }
        });
    }
}