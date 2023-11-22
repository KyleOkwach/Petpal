package com.flow.petpal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flow.petpal.Models.EventModel;
import com.flow.petpal.R;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<EventModel> eventModelArrayList;

    public EventAdapter(@NonNull Context context, ArrayList<EventModel> eventModelArrayList) {
        this.context = context;
        this.eventModelArrayList = eventModelArrayList;
    }

    @NonNull
    @Override
    public EventAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_date, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.Viewholder holder, int position) {
        // to set data to textview of each card layout
        EventModel model = eventModelArrayList.get(position);
        holder.checkupDateTV.setText(model.getEventDate());
        holder.checkupDescriptionTV.setText(model.getEventDescription());
    }

    @Override
    public int getItemCount() {
        return eventModelArrayList.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        private final TextView checkupDateTV;
        private final TextView checkupDescriptionTV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            checkupDateTV = itemView.findViewById(R.id.checkupDateTV);
            checkupDescriptionTV = itemView.findViewById(R.id.checkupDescriptionTV);
        }
    }
}
