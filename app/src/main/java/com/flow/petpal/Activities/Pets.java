package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;
import android.widget.GridView;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.flow.petpal.Adapters.PetAdapter;
import com.flow.petpal.Models.PetModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.flow.petpal.R;
public class Pets extends AppCompatActivity {

    private ConstraintLayout buttonAddPet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        buttonAddPet = findViewById(R.id.buttonAddPet);

        buttonAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pets.this, PetForm.class));
            }
        });


        // -----------------
        // | PETS GRIDVIEW |
        // -----------------

        GridView petGV;

        petGV = findViewById(R.id.idGVpets);
        ArrayList<PetModel> petModelArrayList = new ArrayList<PetModel>();

        petModelArrayList.add(new PetModel(1, "Toby", R.drawable.cat));
        petModelArrayList.add(new PetModel(2, "Falco", R.drawable.bird));

        PetAdapter adapter = new PetAdapter(this, petModelArrayList);
        petGV.setAdapter(adapter);


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
}