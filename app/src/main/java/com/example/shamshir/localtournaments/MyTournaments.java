package com.example.shamshir.localtournaments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyTournaments extends AppCompatActivity {

    DatabaseReference databaseGames;
    DatabaseReference users;
    List<Product> productList;
    List<Product> sortedProductList;
    RecyclerView recyclerView;
    ProductAdapter adapter;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tournaments);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
         userID = pref.getString("idName", null);
        users = FirebaseDatabase.getInstance().getReference("Users");
        loadMyTournaments();

        Bundle bundle = getIntent().getExtras();
        Gson gson = new Gson();
        String jsonString = bundle.getString("allProduct");
        Type type = new TypeToken<List<Product>>() {}.getType();
        productList = gson.fromJson(jsonString, type);



        recyclerView = (RecyclerView) findViewById(R.id.mytournament_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        databaseGames = FirebaseDatabase.getInstance().getReference("Games");

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MyTournaments.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(MyTournaments.this, DetailView.class);
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


    private void loadMyTournaments(){

        users.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                for (DataSnapshot gamesSanpshot : dataSnapshot.getChildren()) {

                    Log.i("eee", "onDataChange: "+gamesSanpshot.getKey());


                    String key = gamesSanpshot.getValue().toString();
                     Integer bb = productList.indexOf(key);
Product myTournament = productList.get(bb);
                    sortedProductList.add(myTournament);

                }


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

                adapter = new ProductAdapter(MyTournaments.this, sortedProductList);
                recyclerView.setAdapter(adapter);
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
//                for (DataSnapshot gamesSanpshot : dataSnapshot.getChildren()) {
//
//
//                    Product game = gamesSanpshot.getValue(Product.class);
//
//                    productList.add(game);
//
//
//                }
//
//
//                Collections.sort(productList, new Comparator<Product>() {
//                    DateFormat f = new SimpleDateFormat("EEE,dd/MMM/yy");
//
//                    @Override
//                    public int compare(Product lhs, Product rhs) {
//                        try {
//                            return f.parse(lhs.getDate()).compareTo(f.parse(rhs.getDate()));
//                        } catch (ParseException e) {
//                            throw new IllegalArgumentException(e);
//                        }
//                    }
//                });
//
//                adapter = new ProductAdapter(MyTournaments.this, productList);
//                recyclerView.setAdapter(adapter);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
    }
}
