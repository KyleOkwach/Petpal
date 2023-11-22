package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.flow.petpal.Adapters.EventAdapter;
import com.flow.petpal.Models.EventModel;
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

import java.util.ArrayList;
import java.util.UUID;

public class Community extends AppCompatActivity {

    private ConstraintLayout buttonBack, buttonSetDate;
    private EditText activityDateET, activityDescriptionET;
    private ProgressBar submitting;
    private RecyclerView activitiesRV;
    private String petID;
    private Calendar calendar;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private  DatabaseReference nutritionRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.community);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.pets:
                        startActivity(new Intent(getApplicationContext(),Pets.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.community:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();
        petID = getIntent().getStringExtra("PetID");

        activityDateET = findViewById(R.id.date);
        activityDescriptionET = findViewById(R.id.description);
        activitiesRV = findViewById(R.id.activitiesRV);

        submitting.setVisibility(View.INVISIBLE);

        calendar = Calendar.getInstance();
        activityDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Populate nutritions list
        shownutritionSchedule();
    }

    private void shownutritionSchedule() {
        nutritionRef = db.getReference()
                .child("Users")
                .child(user.getUid())
                .child("Pets")
                .child(petID)
                .child("Nutrition");
        nutritionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<EventModel> eventModelArrayList = new ArrayList<EventModel>();

                for (DataSnapshot petSnapshot : dataSnapshot.getChildren()) {
                    // Access each pet item
                    String nutritionID = petSnapshot.getKey();
                    String nutritionDate = petSnapshot.child("eventDate").getValue(String.class);
                    String nutritionDescription = petSnapshot.child("eventDescription").getValue(String.class);

                    eventModelArrayList.add(new EventModel(nutritionID, nutritionDate, nutritionDescription, petID));
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

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the EditText field with the selected date
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", getResources().getConfiguration().locale);
                        activityDateET.setText(dateFormat.format(calendar.getTime()));
                    }
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
    }
}