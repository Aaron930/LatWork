package com.example.asus.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity{
    private Button logOutBtn;
    private TextView profileName,profileEmail,profileUid;
    private ImageView ImgProfilePicture;
    private FirebaseAuth mAuth;

    //fireBaseData
    private String UserName, UserEmail, UserUid;
    private Uri UserPhotoUrl;
    private Boolean UserEmailVerified;

    private static Handler mHandler;
    BitmapProgress bitmapProgress= new BitmapProgress();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();



        initView();

        if (user != null) {
            //取得使用者名稱
            UserName = user.getDisplayName();
            //取得使用者 email
            UserEmail = user.getEmail();
            //取得使用者頭像位址
            UserPhotoUrl = user.getPhotoUrl();
            //檢查是否有經過 email 驗證
            UserEmailVerified = user.isEmailVerified();
            //取得使用者 uid
            UserUid = user.getUid();

            showUserProfile();
            Log.d("Status","LOG IN");
        }

        logOutBtn.setOnClickListener(mLogOutBtnOnClick);
    }

    private void initView (){
        ImgProfilePicture=findViewById(R.id.profile_picture);
        logOutBtn =findViewById(R.id.log_out_button);
        profileName =findViewById(R.id.profile_name);
        profileEmail=findViewById(R.id.profile_email);
        profileUid=findViewById(R.id.profile_uid);
    }

    private View.OnClickListener mLogOutBtnOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setClass(ProfileActivity.this,MainActivity.class);
            startActivity(intent);
        }
    };

    private void showUserProfile (){


        if ( UserPhotoUrl != null) {
//            mHandler = new Handler(){
//                @Override
//                public void handleMessage(Message msg) {
//                    switch(msg.what){
//                        case 1:
//
//                            ImgProfilePicture.setImageBitmap(BitmapProgress.DownloadWebPicture.getImg());
//                            break;
//                    }
//                    super.handleMessage(msg);
//                }
//            };
//            loadPic.handleWebPic(url, mHandler);

            Glide.with(ProfileActivity.this)
                    .load(UserPhotoUrl.toString())
                    .crossFade()
                    .into(ImgProfilePicture);
        }
        profileName.setText(UserName);
        profileEmail.setText(UserEmail);
        profileUid.setText(UserUid);
    }
}
