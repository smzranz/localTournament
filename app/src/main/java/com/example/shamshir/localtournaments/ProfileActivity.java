package com.example.shamshir.localtournaments;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class ProfileActivity extends AppCompatActivity {



    ProfileDetails profileDetails;

    DatabaseReference profileData;

    RecyclerView profileRecyclerView;
    ProfileAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        profileRecyclerView = (RecyclerView) findViewById(R.id.profileRecyclerView);
        profileRecyclerView.setHasFixedSize(true);
        profileRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        boolean userLogin = pref.getBoolean("userLogged", false);
            String userID = pref.getString("idName","");

        profileData = FirebaseDatabase.getInstance().getReference("Users/"+userID);

    }


    private void loadProfileData(){

        profileData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Log.i(String.valueOf(dataSnapshot.getValue()), "onDataChange: ");
               // for(DataSnapshot gamesSanpshot : dataSnapshot.getChildren()){



                    ProfileDetails game = dataSnapshot.getValue(ProfileDetails.class);

                    profileDetails = game;



              //  }


                adapter = new ProfileAdapter(ProfileActivity.this, profileDetails);
                profileRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();


        loadProfileData();
    }
}
