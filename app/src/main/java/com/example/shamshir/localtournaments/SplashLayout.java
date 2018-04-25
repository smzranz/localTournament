package com.example.shamshir.localtournaments;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashLayout extends AppCompatActivity {
    private  static  int SPLASH_SCREEN_TIME = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent homeIntent = new Intent(SplashLayout.this,MainActivity.class);

                startActivity(homeIntent);
                finish();

            }
        },SPLASH_SCREEN_TIME);
    }
}
