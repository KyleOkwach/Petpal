package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

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

        GridView petGV;

        petGV = findViewById(R.id.idGVpets);
        ArrayList<PetModel> courseModelArrayList = new ArrayList<PetModel>();

        courseModelArrayList.add(new PetModel("Toby", R.drawable.cat));
        courseModelArrayList.add(new PetModel("Falco", R.drawable.bird));

        PetAdapter adapter = new PetAdapter(this, courseModelArrayList);
        petGV.setAdapter(adapter);
    }
}