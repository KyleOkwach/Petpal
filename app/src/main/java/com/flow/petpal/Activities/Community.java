package com.flow.petpal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flow.petpal.Adapters.FriendRequestsAdapter;
import com.flow.petpal.Adapters.FriendsAdapter;
import com.flow.petpal.Adapters.UsersAdapter;
import com.flow.petpal.Models.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.flow.petpal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Community extends AppCompatActivity {
    
    private ProgressBar progressBarCommunity, progressBarFriends, progressBarRequests;
    private ConstraintLayout buttonCommunity, buttonFriends, buttonRequests;
    private RecyclerView communityRV, friendsRV, requestsRV;
    private TextView communityTV, friendsTV, requestsTV;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference userRef;
    private  DatabaseReference usersRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.community);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        progressBarCommunity = findViewById(R.id.progressBar);
        progressBarCommunity.setVisibility(View.INVISIBLE);

        buttonCommunity = findViewById(R.id.buttonCommunity);
        buttonFriends = findViewById(R.id.buttonFriends);
        buttonRequests = findViewById(R.id.buttonRequests);

        communityRV = findViewById(R.id.communityRV);
        friendsRV = findViewById(R.id.friendsRV);
        requestsRV = findViewById(R.id.requestsRV);
        
        friendsTV = findViewById(R.id.friendsTV);
        communityTV = findViewById(R.id.communityTV);
        requestsTV = findViewById(R.id.requestsTV);

        // Change tabs
        buttonCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change layout visibility
                communityRV.setVisibility(View.VISIBLE);
                friendsRV.setVisibility(View.GONE);
                requestsRV.setVisibility(View.GONE);

                // Change button backgrounds
                buttonCommunity.setBackgroundResource(R.drawable.button_basic);
                buttonFriends.setBackgroundResource(R.drawable.background_white);
                buttonRequests.setBackgroundResource(R.drawable.background_white);

                // Change color of button text
                communityTV.setTextColor(getResources().getColor(R.color.secondary));
                friendsTV.setTextColor(getResources().getColor(R.color.text));
                requestsTV.setTextColor(getResources().getColor(R.color.text));

                showUsers();
            }
        });

        buttonFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change layout visibility
                communityRV.setVisibility(View.GONE);
                friendsRV.setVisibility(View.VISIBLE);
                requestsRV.setVisibility(View.GONE);

                // Change button backgrounds
                buttonCommunity.setBackgroundResource(R.drawable.background_white);
                buttonFriends.setBackgroundResource(R.drawable.button_basic);
                buttonRequests.setBackgroundResource(R.drawable.background_white);

                // Change color of button text
                communityTV.setTextColor(getResources().getColor(R.color.text));
                friendsTV.setTextColor(getResources().getColor(R.color.secondary));
                requestsTV.setTextColor(getResources().getColor(R.color.text));

                showFriends();
            }
        });

        buttonRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change layout visibility
                communityRV.setVisibility(View.GONE);
                friendsRV.setVisibility(View.GONE);
                requestsRV.setVisibility(View.VISIBLE);

                // Change button backgrounds
                buttonCommunity.setBackgroundResource(R.drawable.background_white);
                buttonFriends.setBackgroundResource(R.drawable.background_white);
                buttonRequests.setBackgroundResource(R.drawable.button_basic);
                
                // Change color of button text
                communityTV.setTextColor(getResources().getColor(R.color.text));
                friendsTV.setTextColor(getResources().getColor(R.color.text));
                requestsTV.setTextColor(getResources().getColor(R.color.secondary));

                showRequests();
            }
        });

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.pets:
                        startActivity(new Intent(getApplicationContext(),Pets.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.community:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();
        
        showUsers();
    }

    private void showUsers() {
        progressBarCommunity.setVisibility(View.VISIBLE);  // Show progress bar

        usersRef = db.getReference()
                .child("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<UserModel> usersModelArrayList = new ArrayList<UserModel>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Access each pet item
                    String userID = userSnapshot.getKey();
                    String username = userSnapshot.child("userName").getValue(String.class);
                    String firstName = userSnapshot.child("firstName").getValue(String.class);
                    String lastName = userSnapshot.child("lastName").getValue(String.class);
                    String imageUri = userSnapshot.child("imageUri").getValue(String.class);

                    if(userID != user.getUid()) {
                        usersModelArrayList.add(new UserModel(userID, firstName, lastName, username, imageUri));
                    }
                }

                UsersAdapter usersAdapter = new UsersAdapter(getApplicationContext(), usersModelArrayList);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                // in below two lines we are setting layoutmanager and adapter to our recycler view.
                communityRV.setLayoutManager(linearLayoutManager);
                communityRV.setAdapter(usersAdapter);

                progressBarCommunity.setVisibility(View.INVISIBLE);  // Hide progress bar
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors while fetching data
                Log.e("FirebaseError", "Error fetching users: " + databaseError.getMessage());
                progressBarCommunity.setVisibility(View.INVISIBLE);  // Hide progress bar
            }
        });
    }

    private void showRequests() {
        progressBarCommunity.setVisibility(View.VISIBLE);  // Show progress bar

        usersRef = db.getReference()
                .child("Users")
                .child(user.getUid())
                .child("Friends")
                .child("Pending");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalUsers = (int) dataSnapshot.getChildrenCount();
                AtomicInteger usersFetched = new AtomicInteger(0);
                ArrayList<UserModel> usersModelArrayList = new ArrayList<>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    userRef = db.getReference("User").child(userSnapshot.getKey());
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Fetch user data
                            userRef = db.getReference("User")
                                    .child(userSnapshot.getKey());
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String userID = snapshot.getKey();
                                    String username = snapshot.child("userName").getValue(String.class);
                                    String firstName = snapshot.child("firstName").getValue(String.class);
                                    String lastName = snapshot.child("lastName").getValue(String.class);
                                    String imageUri = snapshot.child("imageUri").getValue(String.class);

                                    if(userID != user.getUid()) {
                                        usersModelArrayList.add(new UserModel(userID, firstName, lastName, username, imageUri));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("FirebaseError", "Error fetching user: " + error.getMessage());
                                }
                            });
                            // Check if all users are fetched and set the adapter
                            usersFetched.getAndIncrement();
                            if (usersFetched.get() == totalUsers) {
                                FriendRequestsAdapter friendRequestsAdapter = new FriendRequestsAdapter(getApplicationContext(), usersModelArrayList);
                                requestsRV.setAdapter(friendRequestsAdapter);
                                progressBarCommunity.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle cancellation
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors while fetching data
            }
        });


    }

    private void showFriends() {
        progressBarCommunity.setVisibility(View.VISIBLE);  // Show progress bar

        usersRef = db.getReference()
                .child("Users")
                .child(user.getUid())
                .child("Friends");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<UserModel> usersModelArrayList = new ArrayList<UserModel>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Access each pet item
                    String userID = userSnapshot.getKey();
                    String username = userSnapshot.child("userName").getValue(String.class);
                    String firstName = userSnapshot.child("firstName").getValue(String.class);
                    String lastName = userSnapshot.child("lastName").getValue(String.class);
                    String imageUri = userSnapshot.child("imageUri").getValue(String.class);

                    if(userID != user.getUid()) {
                        usersModelArrayList.add(new UserModel(userID, firstName, lastName, username, imageUri));
                    }
                }

                FriendsAdapter friendsAdapter = new FriendsAdapter(getApplicationContext(), usersModelArrayList);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                // in below two lines we are setting layoutmanager and adapter to our recycler view.
                friendsRV.setLayoutManager(linearLayoutManager);
                friendsRV.setAdapter(friendsAdapter);

                progressBarCommunity.setVisibility(View.INVISIBLE);  // Hide progress bar
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors while fetching data
                Log.e("FirebaseError", "Error fetching users: " + databaseError.getMessage());
                progressBarCommunity.setVisibility(View.INVISIBLE);  // Hide progress bar
            }
        });
    }
}