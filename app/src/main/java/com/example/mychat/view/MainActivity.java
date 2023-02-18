package com.example.mychat.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mychat.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // on below line we are
                // creating a new intent
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                // on below line we are
                // starting a new activity.
                startActivity(i);
                // on the below line we are finishing
                // our current activity.
                finish();
            }
        }, 5000);


    }
}