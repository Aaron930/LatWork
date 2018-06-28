package com.example.asus.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class SplashActivity extends AppCompatActivity {
    private ImageView mImageView;
    BitmapProgress bitmapProgress = new BitmapProgress();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        mImageView =findViewById(R.id.splash_background);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mImageView.setImageBitmap(
                bitmapProgress.decodeSampledBitmapFromResource(getResources(), R.drawable.screen, 200, 200));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent().setClass(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);
    }

}