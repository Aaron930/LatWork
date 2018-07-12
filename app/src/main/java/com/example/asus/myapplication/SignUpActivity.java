package com.example.asus.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String account;
    private String password;
    private TextInputLayout accoutLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout nicknameLayout;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button signUpBtn,FbSignUpBtn;
    private FirebaseUser user;
    private TextView SignInTxt;

    private static final String TAG = FacebookActivity.class.getSimpleName();
    private LoginManager loginManager;
    private CallbackManager callbackManager;

    MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        initView();
    }

    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        FbSignUpBtn=findViewById(R.id.sign_up_facebook_button);
        SignInTxt = findViewById(R.id.sign_in_account_txt);
        accountEdit = (EditText) findViewById(R.id.sign_up_account_txt);
        passwordEdit = (EditText) findViewById(R.id.sign_up_password_txt);
        accoutLayout = (TextInputLayout) findViewById(R.id.sign_up_account_layout);
        passwordLayout = (TextInputLayout) findViewById(R.id.sign_up_password_layout);
        nicknameLayout = (TextInputLayout) findViewById(R.id.sign_up_nickname_layout);
        passwordLayout.setErrorEnabled(true);
        accoutLayout.setErrorEnabled(true);
        nicknameLayout.setErrorEnabled(true);
        signUpBtn = (Button) findViewById(R.id.sign_up_button);

        signUpBtn.setOnClickListener(mSignUpBtnOnClick);
        SignInTxt.setOnClickListener(mSignInTxtOnClick);
        FbSignUpBtn.setOnClickListener(mFbSignUpBtnonClick);
    }

    private View.OnClickListener mSignUpBtnOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String account = accountEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            if(TextUtils.isEmpty(account)){
                accoutLayout.setError(getString(R.string.plz_input_accout));
                passwordLayout.setError("");
                return;
            }
            if(TextUtils.isEmpty(password)){
                accoutLayout.setError("");
                passwordLayout.setError(getString(R.string.plz_input_pw));
                return;
            }
            accoutLayout.setError("");
            passwordLayout.setError("");
            mAuth.createUserWithEmailAndPassword(account, password)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setClass(SignUpActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };

    private View.OnClickListener mSignInTxtOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener mFbSignUpBtnonClick =new View.OnClickListener() {
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
            loginManager.logInWithReadPermissions(SignUpActivity.this, permissions);
            loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult.getAccessToken());

                    Toast.makeText(SignUpActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setClass(SignUpActivity.this, MainActivity.class);
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
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
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