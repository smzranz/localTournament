package com.example.shamshir.localtournaments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.media.Image;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    String[] arrayForSpinner = {"All", "Football", "Cricket", "VolleyBall", "ShootOut", "Other"};
    String[] arrayForDistSpinner = {"All", "Alappuzha",
            "Ernakulam",
            "Idukki",
            "Kannur",
            "Kasaragod",
            "Kollam",
            "Kottayam",
            "Kozhikode",
            "Malappuram",
            "Palakkad",
            "Pathanamthitta",
            "Thiruvananthapuram",
            "Thrissur",
            "Wayanad", "Other"};

    private DrawerLayout mDrawerLayout;
    private DatabaseReference mMessagesReference;
    private FirebaseDatabase mDatabase;
    
    List<Product> productList;
    List<Product> sortedProductList;
    List<Product> tempProductList;

    //the recyclerview
    RecyclerView recyclerView;
    ProductAdapter adapter;


    Boolean isProfileSelected = false;
    DatabaseReference databaseGames;
    DatabaseReference Users;

    Button navButton;
    Button menuButton;
    Button sortButton;
    ImageButton addTournamentBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.activity_main_layout);



        // getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        mMessagesReference = FirebaseDatabase.getInstance().getReference("messages");

        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");

        navButton = (Button) findViewById(R.id.navButton);
        navButton.setBackgroundResource(R.drawable.filtericon);
        navButton.invalidate();

        menuButton = (Button) findViewById(R.id.menuButton);
        menuButton.setBackgroundResource(R.drawable.ic_menu_black_24dp);
        menuButton.invalidate();


        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawerLayout.openDrawer(GravityCompat.START);

            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFilterlayout();
//                Message message = new Message("jjjjjjjjj", "shamshir", null);
//                mMessagesReference.push().setValue(message);

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        sortedProductList = new ArrayList<>();
        tempProductList = new ArrayList<>();
        databaseGames = FirebaseDatabase.getInstance().getReference("Games");


        Users = FirebaseDatabase.getInstance().getReference("Users");

        addTournamentBtn = (ImageButton) findViewById(R.id.ActionButton);

        addTournamentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTournamenetPressed();
                //startActivity(new Intent(MainActivity.this, AddTournamentActivity.class));


            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(MainActivity.this, DetailView.class);
                Gson gson = new Gson();

                String json = gson.toJson(productList.get(position));
                Bundle basket = new Bundle();
                basket.putString("json", json);

                intent.putExtras(basket);
                startActivity(intent);


            }

            @Override
            public void onItemLongClick(View view, int position) {

                // ...
            }
        }));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    protected void onStart() {
        super.onStart();


        databaseGames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                productList.clear();

                for (DataSnapshot gamesSanpshot : dataSnapshot.getChildren()) {


                    Product game = gamesSanpshot.getValue(Product.class);

                    productList.add(game);


                }


                Collections.sort(productList, new Comparator<Product>() {
                    DateFormat f = new SimpleDateFormat("EEE,dd/MMM/yy");

                    @Override
                    public int compare(Product lhs, Product rhs) {
                        try {
                            return f.parse(lhs.getDate()).compareTo(f.parse(rhs.getDate()));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });

                adapter = new ProductAdapter(MainActivity.this, productList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    Integer gameSinnerPosition = 0;
    Integer districtSinnerPosition = 0;

    private void showFilterlayout() {


        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.fragment_filter, null);
        final Spinner gameSpinner = alertLayout.findViewById(R.id.gameSort);

        final Spinner districtSpinner = alertLayout.findViewById(R.id.placeSort);

        Log.i(gameSpinner + "jjj", "showFilterlayout: ");

        final Button Apply = alertLayout.findViewById(R.id.filter_apply);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayForSpinner);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameSpinner.setAdapter(dataAdapter);
        gameSpinner.setSelection(gameSinnerPosition);


        ArrayAdapter<String> distdataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayForDistSpinner);
        distdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(distdataAdapter);
        districtSpinner.setSelection(districtSinnerPosition);


        Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                gameSinnerPosition = gameSpinner.getSelectedItemPosition();
                districtSinnerPosition = districtSpinner.getSelectedItemPosition();
                applyBtnPressed(gameSpinner.getSelectedItem().toString().trim(), districtSpinner.getSelectedItem().toString().trim());

            }
        });


        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Filter");

        alert.setView(alertLayout);

        alert.setCancelable(true);
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        dialog = alert.create();

        dialog.show();


    }


    private void applyBtnPressed(final String game, final String district) {

        Log.i("TEST", "applyBtnPressed for"+game+district);
        if (game.equals("All") && district.equals("All")) {

            sortedProductList = productList;
        } else {

            tempProductList.clear();
            sortedProductList.clear();

            if (district.equals("All")) {
                for (int i = 0; i < productList.size(); i++) {
                    if (game.equals(productList.get(i).getSelectedGame().toString())) {
                        sortedProductList.add(productList.get(i));
                    }
                }
            } else if (game.equals("All")) {
                for (int i = 0; i < productList.size(); i++) {
                    if (district.equals(productList.get(i).getDistrict().toString())) {
                        sortedProductList.add(productList.get(i));
                    }
                }

            } else {



                tempProductList.clear();
                sortedProductList.clear();
                for (int i = 0; i < productList.size(); i++) {
                    if(game.equals(productList.get(i).getSelectedGame().toString())) {
                        sortedProductList.add(productList.get(i));
                    }
                }

                for (int i = 0; i < sortedProductList.size(); i++) {
                    if (district.equals(sortedProductList.get(i).getDistrict().toString())) {
                        tempProductList.add(sortedProductList.get(i));
                    }
                }

                sortedProductList.clear();
                sortedProductList.addAll(tempProductList);
                // tempProductList.clear();

            }


        }


        if (sortedProductList.size() == 0) {

            Toast.makeText(MainActivity.this,
                    "NO DATA", Toast.LENGTH_LONG).show();


        } else {

            Collections.sort(sortedProductList, new Comparator<Product>() {
                DateFormat f = new SimpleDateFormat("EEE,dd/MMM/yy");

                @Override
                public int compare(Product lhs, Product rhs) {
                    try {
                        return f.parse(lhs.getDate()).compareTo(f.parse(rhs.getDate()));
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });

            adapter = new ProductAdapter(MainActivity.this, sortedProductList);
            recyclerView.setAdapter(adapter);

        }


    }

    AlertDialog dialog;

    private void showLoginLayout() {


        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.login_popup, null);
        final EditText Username = alertLayout.findViewById(R.id.userName);
        final EditText Password = alertLayout.findViewById(R.id.password);
        final Button loginBtn = alertLayout.findViewById(R.id.loginBtn);
        final Button signUpBtn = alertLayout.findViewById(R.id.signupBtn);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                String userName = Username.getText().toString().trim();
                String userPassword = Password.getText().toString().trim();
                Log.i(userName, "userName");
                Log.i(userPassword, "userPassword");

                checkForUser(userName, userPassword);

            }
        });
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Login");

        // alert.setTitle("Info");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        dialog = alert.create();

        dialog.show();

        // dialog.getWindow().setLayout(width-150, 1350);

    }


    private void checkForUser(String number, final String pass) {
        Users.orderByChild("mobileNumber").equalTo(number).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    for (DataSnapshot Users : dataSnapshot.getChildren()) {


                        if (Users.child("password").getValue().equals(pass)) {


                            Log.i("TAG", "getChildren: " + Users.child("password").getValue());
                            Log.i("TAG", "getKey: " + Users.getKey());

                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("userLogged", true);
                            editor.putString("idName", Users.getKey());
                            editor.apply();

                            if (isProfileSelected) {
                                isProfileSelected = false;
                                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                            } else {
                                startActivity(new Intent(MainActivity.this, AddTournamentActivity.class));

                            }

                        } else {

                            Toast.makeText(MainActivity.this,
                                    "Invalid Password", Toast.LENGTH_LONG).show();


                        }


                    }


                } else {
                    Toast.makeText(MainActivity.this,
                            "Please enter a registered mobile number", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


                Toast.makeText(MainActivity.this,
                        "ERROR : " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


    private void showLogOutAlert() {


        new MaterialDialog.Builder(MainActivity.this)
                .title("LOGOUT")
                .content("Do you really want to logout")
                .positiveText("Yes")
                .negativeText("No")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {

                        if (which == DialogAction.NEGATIVE) {


                        } else {

                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.remove("userLogged"); // will delete key key_name3
                            editor.remove("idName");
                            editor.apply();


                            Toast.makeText(MainActivity.this,
                                    "Logout successfull", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.my_tournaments) {
            myTournaments();


        } else if (id == R.id.add_tournaments) {




            addTournamenetPressed();


        } else if (id == R.id.my_requests) {

        } else if (id == R.id.my_profile) {
            profileClicked();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_aboutus) {

        }


        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;

    }


    private  void myTournaments(){

        Intent intent = new Intent(MainActivity.this, MyTournaments.class);
        Gson gson = new Gson();

        String json = gson.toJson(productList);
        Bundle basket = new Bundle();
        basket.putString("allProduct", json);

        intent.putExtras(basket);
        startActivity(intent);

    }
    private void addTournamenetPressed() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        boolean userLogin = pref.getBoolean("userLogged", false);
        isProfileSelected = false;
        if (userLogin) {
            startActivity(new Intent(MainActivity.this, AddTournamentActivity.class));
        } else {
            showLoginLayout();
        }

    }

    private void profileClicked() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        boolean userLogin = pref.getBoolean("userLogged", false);

        isProfileSelected = true;

        if (userLogin) {

            startActivity(new Intent(MainActivity.this, ProfileActivity.class));

//showLogOutAlert();

        } else {

            showLoginLayout();
        }

    }
}
