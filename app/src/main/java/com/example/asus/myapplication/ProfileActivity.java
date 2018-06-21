package com.example.asus.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity{
    private Button logOutBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);

        // init LoginManager & CallbackManager
        logOutBtn =findViewById(R.id.log_out_button);
        logOutBtn.setOnClickListener(mLogOutBtnOnClick);
    }

    private View.OnClickListener mLogOutBtnOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
//            Glide.with(ProfileActivity.this)
//                    .load(R.drawable.ic_user_60dp)
//                    .crossFade();
//                    .into(mImgPhoto);

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setClass(ProfileActivity.this,MainActivity.class);
            startActivity(intent);
        }
    };
}
