package com.flow.petpal.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.flow.petpal.Models.PetModel;
import com.flow.petpal.R;

import java.util.ArrayList;

public class PetAdapter extends ArrayAdapter<PetModel> {

    public PetAdapter(@NonNull Context context, ArrayList<PetModel> courseModelArrayList) {
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
        TextView courseTV = listitemView.findViewById(R.id.idTVCourse);
        ImageView courseIV = listitemView.findViewById(R.id.idIVcourse);

        courseTV.setText(petModel.getPet_name());
        courseIV.setImageResource(petModel.getImgid());
        return listitemView;
    }
}