package com.flow.petpal.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.flow.petpal.Adapters.PetAdapter;
import com.flow.petpal.Models.PetModel;
import com.flow.petpal.R;

import java.util.ArrayList;

public class PetDialog extends AppCompatActivity {

    private ConstraintLayout buttonCancel;
    private TextView modeTV;
    private String dialogMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_dialog);

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

        petModelArrayList.add(new PetModel(1, "Toby", R.drawable.cat));
        petModelArrayList.add(new PetModel(2, "Falco", R.drawable.bird));

        PetAdapter adapter = new PetAdapter(this, petModelArrayList);
        petGV.setAdapter(adapter);
    }
}