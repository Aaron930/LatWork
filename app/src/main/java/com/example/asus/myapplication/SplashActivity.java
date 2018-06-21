package com.example.asus.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;


public class SplashActivity extends AppCompatActivity {
//    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);



//        imageView = findViewById(R.id.splash_screen);
//
//        imageView.setScaleType(ImageView.ScaleType.CENTER);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent().setClass(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }
}