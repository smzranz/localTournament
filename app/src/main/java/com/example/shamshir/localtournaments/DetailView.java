package com.example.shamshir.localtournaments;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailView extends AppCompatActivity {

    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;
    detailViewAdapter adapter;
    ProfileDetails profileDetails;
    Button requsetBtn;
    Button navButton;

    DatabaseReference databaseProfile;
    DatabaseReference databaseGames;

    String tournamentTitle ;
    String place ;
    String tournamentDate ;
    String price ;
    String gameSelected;
    String contact;
    String totalTeams;
    String moreDetails;
    String imageUrl;
    String cliendId;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = getIntent().getExtras();
        Gson gson = new Gson();
        String jsonString = bundle.getString("json");
        Product json = gson.fromJson(jsonString,Product.class);
        String place = json.getPlace();
        String tournamentTitle = json.getTitle();
        String tournamentDate = json.getDate();
        String price = json.getPrice();
        String gameSelected = json.getSelectedGame();
        String contact = json.getContact();
        String totalTeams = json.getTotalTeams();
        String moreDetails = json.getMoreInfo();
        String district = json.getDistrict();
         imageUrl = json.getImage();
         cliendId = json.getClientID();


        Log.i(tournamentTitle,"tournamentTitle");



        setContentView(R.layout.activity_detail_view);

        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        navButton = (Button) findViewById(R.id.navButton);
        navButton.setBackgroundResource(R.drawable.share);
        navButton.setPadding(0,5,0,5);
        navButton.invalidate();

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              shareTournament("","");

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.detailRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();

        databaseGames = FirebaseDatabase.getInstance().getReference("Games");
        databaseProfile = FirebaseDatabase.getInstance().getReference("Users");

        requsetBtn = (Button) findViewById(R.id.requestBtn);

        requsetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                boolean userLogin = pref.getBoolean("userLogged", false);

                if (userLogin) {
                     userID = pref.getString("idName","");

                    new MaterialDialog.Builder(DetailView.this)
                            .title(R.string.app_name)
                            .content(R.string.app_name)
                            .positiveText("Yes")
                            .negativeText("No")
                            .onAny(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {

                                    if (which == DialogAction.NEGATIVE){


                                    }else{

//();
                                    }
                                }
                            })
                            .show();

                } else {

                    Toast.makeText(DetailView.this,
                            "Please login to make a Request", Toast.LENGTH_LONG).show();



                }






            }
        });

        Product game = new Product("", tournamentTitle, place, tournamentDate,gameSelected, price, imageUrl,contact,totalTeams,moreDetails,district,cliendId);

        productList.add(json);


        adapter = new detailViewAdapter(DetailView.this, json);
        recyclerView.setAdapter(adapter);
    }
    String userID;

private  void sendRequest(){


loadProfileData(userID);


}


    @Override
    protected void onResume() {
        super.onResume();


    }

    private void loadProfileData(String userID){

        databaseProfile.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Log.i(String.valueOf(dataSnapshot.getValue()), "onDataChange: ");
                // for(DataSnapshot gamesSanpshot : dataSnapshot.getChildren()){



                ProfileDetails game = dataSnapshot.getValue(ProfileDetails.class);

                profileDetails = game;


                RequestDetails request = new RequestDetails(profileDetails.getId(),profileDetails.getName(),profileDetails.getMobileNumber(),profileDetails.getClubName());


                databaseProfile.child(cliendId).child("Requests").setValue(request);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    protected void onStart() {
        super.onStart();






//        databaseGames.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                productList.clear();
//
//                for(DataSnapshot gamesSanpshot : dataSnapshot.getChildren()){
//
//
//
//                    Product game = gamesSanpshot.getValue(Product.class);
//
//                    productList.add(game);
//
//
//
//                }
//
//                Collections.reverse(productList);
//                adapter = new detailViewAdapter(DetailView.this, productList);
//                recyclerView.setAdapter(adapter);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    }




 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            // do something here
            shareTournament("http://s1.dmcdn.net/hxdt6/x720-qef.jpg","Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
        }
        return super.onOptionsItemSelected(item);
    }
    private void shareTournament(String imageURL , String title){


        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.setType("*/*");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_TEXT, imageURL);
        startActivity(Intent.createChooser(shareIntent, "Select App to Share Text and Image"));


    }





}
