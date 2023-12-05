package com.flow.petpal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flow.petpal.Models.FriendModel;
import com.flow.petpal.Models.UserModel;
import com.flow.petpal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.Viewholder> {
    private final Context context;
    private final ArrayList<UserModel> userModelArrayList;

    public FriendsAdapter(Context context, ArrayList<UserModel> userModelArrayList) {
        this.context = context;
        this.userModelArrayList = userModelArrayList;
    }

    @NonNull
    @Override
    public FriendsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_friend, parent, false);
        return new FriendsAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.Viewholder holder, int position) {
        // to set data to textview of each card layout
        UserModel model = userModelArrayList.get(position);
        holder.usernameTV.setText(model.getUserName());

        Glide.with(holder.itemView.getContext())
                .load(model.getImageUri())
                .placeholder(R.drawable.image_broken)
                .error(R.drawable.image_broken)
                .into(holder.profileIV);

    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        private final TextView usernameTV, emailTV;
        private final ImageView profileIV;
        private final FirebaseAuth auth;
        private final FirebaseUser user;
        private final FirebaseDatabase db;
        private final DatabaseReference ref;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            usernameTV = itemView.findViewById(R.id.userNameTV);
            emailTV = itemView.findViewById(R.id.emailTV);
            profileIV = itemView.findViewById(R.id.profileIV);
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            db = FirebaseDatabase.getInstance();
            ref = db.getReference("Users");
        }
    }
}