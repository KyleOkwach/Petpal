package com.flow.petpal.Adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.flow.petpal.Activities.PetHome;
import com.flow.petpal.Models.PetModel;
import com.flow.petpal.R;

import java.util.ArrayList;

public class PetAdapterDialog extends ArrayAdapter<PetModel> {

    public PetAdapterDialog(@NonNull Context context, ArrayList<PetModel> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
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
        ImageView petIV = listitemView.findViewById(R.id.petImage);
        ConstraintLayout petLayout = listitemView.findViewById(R.id.petLayout);

        petTV.setText(petModel.getPet_name());

        // set background image
        Drawable backgroundDrawable = ContextCompat.getDrawable(this.getContext(), petModel.getImgid());
        petIV.setImageDrawable(backgroundDrawable);
        // petLayout.setBackground(backgroundDrawable);

        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent petIntent = new Intent(getContext(), PetHome.class);
                petIntent.putExtra("ID", petModel.getPet_ID());
                petIntent.putExtra("Name", petModel.getPet_name());
                getContext().startActivity(petIntent);
            }
        });

        return listitemView;
    }
}