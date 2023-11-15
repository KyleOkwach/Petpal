package com.flow.petpal.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flow.petpal.R;

public class PetHome extends AppCompatActivity {

    private ConstraintLayout buttonBack;
    private int petID;
    private  String petName;
    private TextView petNameTV, petSpeciesTV, petAgeTV;
    private ImageView petIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_home);

        petNameTV = findViewById(R.id.petNameTV);
        petSpeciesTV = findViewById(R.id.petSpeciesTV);
        petAgeTV = findViewById(R.id.petAgeTV);

        // Pet Information
        petID = getIntent().getIntExtra("ID", 0);
        petName = getIntent().getStringExtra("Name");

        petNameTV.setText(petName);


        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}