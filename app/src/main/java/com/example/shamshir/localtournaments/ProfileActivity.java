package com.example.shamshir.localtournaments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

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


        profileRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ProfileActivity.this, profileRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (position == 6) {

                    showLogOutAlert();
                }


            }
            @Override
            public void onItemLongClick(View view, int position) {

                // ...
            }
        }));

    }

    private void showLogOutAlert() {


        new MaterialDialog.Builder(ProfileActivity.this)
                .title("LOGOUT")
                .content("Do you really want to logout")
                .positiveText("Yes")
                .negativeText("No")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {

                        if (which == DialogAction.NEGATIVE) {

goBack();
                        } else {

                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.remove("userLogged"); // will delete key key_name3
                            editor.remove("idName");
                            editor.apply();


                            Toast.makeText(ProfileActivity.this,
                                    "Logout successfull", Toast.LENGTH_LONG).show();
                            goBack();
                        }
                    }
                })
                .show();

    }

    public void goBack(){

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }

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
