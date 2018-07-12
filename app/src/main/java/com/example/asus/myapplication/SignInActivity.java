package com.example.asus.myapplication;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String account;
    private String password;
    private TextInputLayout accoutLayout;
    private TextInputLayout passwordLayout;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button loginBtn, FBloginBtn;
    private FirebaseUser user;

    private static final String TAG = FacebookActivity.class.getSimpleName();
    private LoginManager loginManager;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        // init facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        // init LoginManager & CallbackManager
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        initView();
        loginBtn.setOnClickListener(loginBtnOnClickListener);
        FBloginBtn.setOnClickListener(FBloginBtnOnClickListener);


//        FBloginBtn.setReadPermissions("email", "public_profile");
//        FBloginBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d("TAG", "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d("TAG", "facebook:onCancel");
//                // ...
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d("TAG", "facebook:onError", error);
//                // ...
//            }
//        });
    }

    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        FBloginBtn = findViewById(R.id.sign_in_facebook_button);
        loginBtn = findViewById(R.id.sign_in_button);
        accountEdit = (EditText) findViewById(R.id.sign_in_account_txt);
        passwordEdit = (EditText) findViewById(R.id.sign_in_password_txt);
        accoutLayout = (TextInputLayout) findViewById(R.id.sign_in_account_layout);
        passwordLayout = (TextInputLayout) findViewById(R.id.sign_in_password_layout);
        passwordLayout.setErrorEnabled(true);
        accoutLayout.setErrorEnabled(true);
    }

    private View.OnClickListener loginBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String account = accountEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            if (TextUtils.isEmpty(account)) {
                accoutLayout.setError(getString(R.string.plz_input_accout));
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwordLayout.setError(getString(R.string.plz_input_pw));
                return;
            }
            accoutLayout.setError("");
            passwordLayout.setError("");
            mAuth.signInWithEmailAndPassword(account, password)
                    .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignInActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setClass(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };

    private View.OnClickListener FBloginBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // 設定FB login的顯示方式 ; 預設是：NATIVE_WITH_FALLBACK
            /**
             * 1. NATIVE_WITH_FALLBACK
             * 2. NATIVE_ONLY
             * 3. KATANA_ONLY
             * 4. WEB_ONLY
             * 5. WEB_VIEW_ONLY
             * 6. DEVICE_AUTH
             */
            loginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
            // 設定要跟用戶取得的權限，以下3個是基本可以取得，不需要經過FB的審核
            List<String> permissions = new ArrayList<>();
            permissions.add("public_profile");
            permissions.add("email");
            permissions.add("user_friends");

            // 設定要讀取的權限
            loginManager.logInWithReadPermissions(SignInActivity.this, permissions);
            loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    // 登入成功
                    // 透過GraphRequest來取得用戶的Facebook資訊
//                    GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                        @Override
//                        public void onCompleted(JSONObject object, GraphResponse response) {
//                            try {
//                                if (response.getConnection().getResponseCode() == 200) {
//                                    long id = object.getLong("id");
//                                    String name = object.getString("name");
//                                    String email = object.getString("email");
//                                    Log.d(TAG, "Facebook id:" + id);
//                                    Log.d(TAG, "Facebook name:" + name);
//                                    Log.d(TAG, "Facebook email:" + email);
//                                    // 此時如果登入成功，就可以順便取得用戶大頭照
//                                    Profile profile = Profile.getCurrentProfile();
//                                    // 設定大頭照大小
//                                    Uri userPhoto = profile.getProfilePictureUri(300, 300);
//                                    Glide.with(SignInActivity.this)
//                                            .load(userPhoto.toString())
//                                            .crossFade();
////                                            .into(mImgPhoto);
////                                    mTextDescription.setText(String.format(Locale.TAIWAN, "Name:%s\nE-mail:%s", name, email));
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                    // https://developers.facebook.com/docs/android/graph?locale=zh_TW
//                    // 如果要取得email，需透過添加參數的方式來獲取(如下)
//                    // 不添加只能取得id & name
//                    Bundle parameters = new Bundle();
//                    parameters.putString("fields", "id,name,email");
//                    graphRequest.setParameters(parameters);
//                    graphRequest.executeAsync();

                    handleFacebookAccessToken(loginResult.getAccessToken());


                    Toast.makeText(SignInActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setClass(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onCancel() {
                    // 用戶取消
                    Log.d(TAG, "Facebook onCancel");
                }

                @Override
                public void onError(FacebookException error) {
                    // 登入失敗
                    Log.d(TAG, "Facebook onError:" + error.toString());
                }
            });
        }
    };

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }// ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
