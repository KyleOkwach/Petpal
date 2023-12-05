package com.flow.petpal.Adapters;


import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.flow.petpal.Activities.MainActivity;
import com.flow.petpal.Activities.PetHome;
import com.flow.petpal.Models.PetModel;
import com.flow.petpal.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PetAdapter extends ArrayAdapter<PetModel> {

    public PetAdapter(@NonNull Context context, ArrayList<PetModel> petModelArrayList) {
        super(context, 0, petModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.card_pet, parent, false);
        }

        PetModel petModel = getItem(position);
        TextView petTV = listitemView.findViewById(R.id.idTVPet);
        TextView speciesTV = listitemView.findViewById(R.id.idTVSpecies);
        ImageView petIV = listitemView.findViewById(R.id.petImage);
        ConstraintLayout petLayout = listitemView.findViewById(R.id.petLayout);

        petTV.setText(petModel.getPet_name());
        speciesTV.setText(petModel.getPetSpecies());

        Glide.with(this.getContext())
                .load(petModel.getPetImageUri())
                .placeholder(R.drawable.image_broken)
                .error(R.drawable.image_broken)
                .into(petIV);

        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent petIntent = new Intent(getContext(), PetHome.class);
                petIntent.putExtra("ID", petModel.getPet_ID());
                petIntent.putExtra("Name", petModel.getPet_name());
                petIntent.putExtra("PetModel", (Parcelable) petModel);
                getContext().startActivity(petIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        return listitemView;
    }
}