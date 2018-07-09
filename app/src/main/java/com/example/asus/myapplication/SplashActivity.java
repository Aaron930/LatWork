package com.example.asus.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener{
    private ImageView mImageView,mLogo;
    private Context context;
    BitmapProgress bitmapProgress = new BitmapProgress();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        context = this;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mImageView =findViewById(R.id.splash_background);
        mLogo =findViewById(R.id.Logo);

        mImageView.setImageBitmap(
                bitmapProgress.decodeSampledBitmapFromResource(getResources(), R.drawable.screen, 200, 200));

//        //imageView 設定動畫元件(透明度調整)
//        Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
//        animationIn.setFillEnabled(true);
//        animationIn.setFillAfter(true);
//        animationIn.setAnimationListener(this);
//        mImageView.setAnimation(animationIn);

        mImageView.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.alpha_in));
        mLogo.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.alpha_in));


//        Animation animationOut =  AnimationUtils.loadAnimation(this, R.anim.alpha_out);
//        animationOut.setFillEnabled(true);
//        animationOut.setFillAfter(true);
//        animationOut.setAnimationListener(this);
//        mImageView.setAnimation(animationOut);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent().setClass(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        startActivity(new Intent(context,MainActivity.class));
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}