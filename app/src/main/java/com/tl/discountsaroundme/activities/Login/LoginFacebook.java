package com.tl.discountsaroundme.activities.Login;


import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tl.discountsaroundme.activities.LoginActivity;

public class  LoginFacebook extends FragmentActivity implements ProgramLogin {
CallbackManager callbackManager;
LoginActivity logBut = new LoginActivity();
LoginActivity logSucc = new LoginActivity();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    public void loginTypeCheck() {
        FacebookSdk.sdkInitialize(getApplicationContext());

        logBut.loginFacebook.setReadPermissions("email", "public_profile");
        callbackManager = CallbackManager.Factory.create();

        logBut.loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "LoginActivity Successful", Toast.LENGTH_LONG).show();
                user = mAuth.getCurrentUser();
                logSucc.loginSuccessful("user", user.getUid().toString());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "LoginActivity Cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "####Error-facebook####", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void signIn() {

    }

}



