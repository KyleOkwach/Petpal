package com.flow.petpal.Adapters;

import android.annotation.SuppressLint;
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

public class FriendRequestsAdapter  extends RecyclerView.Adapter<FriendRequestsAdapter.Viewholder> {
    private final Context context;
    private final ArrayList<UserModel> userModelArrayList;

    public FriendRequestsAdapter(Context context, ArrayList<UserModel> userModelArrayList) {
        this.context = context;
        this.userModelArrayList = userModelArrayList;
    }

    @NonNull
    @Override
    public FriendRequestsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_friend_request, parent, false);
        return new FriendRequestsAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestsAdapter.Viewholder holder, int position) {
        // to set data to textview of each card layout
        UserModel model = userModelArrayList.get(position);
        holder.usernameTV.setText(model.getUserName());

        Glide.with(holder.itemView.getContext())
                .load(model.getImageUri())
                .placeholder(R.drawable.image_broken)
                .error(R.drawable.image_broken)
                .into(holder.profileIV);

        holder.buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add to friends
                FriendModel friend = new FriendModel(model.getUid(), "requester");
                FriendModel friendRequester = new FriendModel(holder.user.getUid(), "responder");
                holder.ref.child(holder.user.getUid()+"/Friends").setValue(friend);
                // On other end
                holder.ref.child(model.getUid()+"/Friends").setValue(friendRequester);

                // Delete from pending
                holder.ref.child(model.getUid()+"/Friends/Pending/"+friend.getUid()).removeValue();
                // On other end
                holder.ref.child(model.getUid()+"/Friends/Pending/"+friendRequester.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(holder.itemView.getContext(), "Friend accepted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete from pending
                FriendModel friend = new FriendModel(model.getUid(), "responder");
                FriendModel friendRequester = new FriendModel(holder.user.getUid(), "requester");
                holder.ref.child(model.getUid()+"/Friends/Pending/"+friend.getUid()).removeValue();
                // On other end
                holder.ref.child(model.getUid()+"/Friends/Pending/"+friendRequester.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(holder.itemView.getContext(), "Friend accepted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        private final TextView usernameTV, emailTV;
        private final ImageView profileIV;
        private final ConstraintLayout buttonAccept, buttonReject;
        private final FirebaseAuth auth;
        private final FirebaseUser user;
        private final FirebaseDatabase db;
        private final DatabaseReference ref;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            usernameTV = itemView.findViewById(R.id.userNameTV);
            emailTV = itemView.findViewById(R.id.emailTV);
            profileIV = itemView.findViewById(R.id.profileIV);
            buttonAccept = itemView.findViewById(R.id.buttonAccept);
            buttonReject = itemView.findViewById(R.id.buttonReject);
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            db = FirebaseDatabase.getInstance();
            ref = db.getReference("Users");
        }
    }
}