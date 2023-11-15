package com.flow.petpal.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.flow.petpal.R;

public class PetForm extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1000;
    private ConstraintLayout buttonUploadImage;
    private ImageView petIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_form);

        buttonUploadImage = findViewById(R.id.buttonUploadImage);
        petIV = findViewById(R.id.petIV);

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {

            if(requestCode == PICK_IMAGE_REQUEST) {
                // Set imageView to selected image URI
                petIV.setImageURI(data.getData());
            }
        }
    }
}