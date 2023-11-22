package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PetHome extends AppCompatActivity {

    private ConstraintLayout buttonBack, buttonGrooming, buttonHealth, buttonNutrition, buttonTraining;
    private TextView petNameTV, petSpeciesTV, petAgeTV;
    private ImageView petIV, petGenderIV;

    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseUser user;
    DatabaseReference petsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_home);

        petNameTV = findViewById(R.id.petNameTV);
        petSpeciesTV = findViewById(R.id.petSpeciesTV);
        petAgeTV = findViewById(R.id.petAgeTV);
        petIV = findViewById(R.id.petIV);
        petGenderIV = findViewById(R.id.petGender);

        buttonGrooming = findViewById(R.id.buttonGrooming);
        buttonHealth = findViewById(R.id.buttonHealth);
        buttonNutrition = findViewById(R.id.buttonNutrition);
        buttonTraining = findViewById(R.id.buttonTraining);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();

        // Pet Information
        String petID;
        petID = getIntent().getStringExtra("ID");
        PetModel petModel = (PetModel) getIntent().getParcelableExtra("PetModel");

        buttonHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groomingIntent = new Intent(PetHome.this, Health.class);
                groomingIntent.putExtra("PetID", petID);
                startActivity(groomingIntent);
            }
        });

        buttonGrooming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groomingIntent = new Intent(PetHome.this, Grooming.class);
                groomingIntent.putExtra("PetID", petID);
                startActivity(groomingIntent);
            }
        });

        buttonTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groomingIntent = new Intent(PetHome.this, Training.class);
                groomingIntent.putExtra("PetID", petID);
                startActivity(groomingIntent);
            }
        });

        // Fetch pet's data
        petsRef = db.getReference().child("Users").child(user.getUid()).child("Pets").child(petID);
        petsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<PetModel> petModelArrayList = new ArrayList<PetModel>();
                String petID, petName, petSpecies, petImage, petGender;
                Date petDob;

                // Access each pet item
                petID = dataSnapshot.getKey(); // Get the unique ID of the pet
                petName = petModel.getPet_name(); // Get the name of the pet
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    petDob = format.parse(petModel.getPetDOB());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                LocalDate birthDate, currentDate;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    birthDate = petDob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    currentDate = LocalDate.now();
                    int age = Period.between(birthDate, currentDate).getYears();
                    petAgeTV.setText(age+" Years old");
                }

                petSpecies = petModel.getPetSpecies();
                petImage = petModel.getPetImageUri();
                petGender = petModel.getPetGender();

                Drawable backgroundDrawable;
                if(petGender.toLowerCase() == "male") {
                    backgroundDrawable = ContextCompat.getDrawable(PetHome.this, R.drawable.gender_male);
                    petGenderIV.setImageDrawable(backgroundDrawable);
                } else if(petGender.toLowerCase() == "female") {
                    backgroundDrawable = ContextCompat.getDrawable(PetHome.this, R.drawable.gender_female);
                    petGenderIV.setImageDrawable(backgroundDrawable);
                }

                if (petImage != null) {
                    Glide.with(PetHome.this)
                        .load(petImage)
                        .placeholder(R.drawable.image_broken)
                        .error(R.drawable.image_broken)
                        .into(petIV);
                } else {
                    // Handle the case where petImage is null
                }
                petIV.setImageURI(Uri.parse(petImage));

                petNameTV.setText(petName);
                petSpeciesTV.setText(petSpecies);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors while fetching data
                Log.e("FirebaseError", "Error fetching pets: " + databaseError.getMessage());
            }
        });

        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}