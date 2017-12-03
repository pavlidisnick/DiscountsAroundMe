package com.tl.discountsaroundme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.activities.Login.LoginDiscount;
import com.tl.discountsaroundme.activities.Login.LoginFacebook;
import com.tl.discountsaroundme.activities.Login.LoginGoogle;
import com.tl.discountsaroundme.activities.Login.ProgramLogin;

public class LoginActivity extends FragmentActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    public static final int RC_SIGN_IN = 9000;
    ProgramLogin logDisc = new LoginDiscount();
    LoginDiscount isFormDisc = new LoginDiscount();
    LoginGoogle logGoogle = new LoginGoogle();
    LoginFacebook logFacebook = new LoginFacebook();
    Button login;
    Button register;
    public LoginButton loginFacebook;
    SignInButton signInButton;



    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference("/shops");
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logDisc = null;
        isFormDisc = null;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Facebook login
        FacebookSdk.sdkInitialize(getApplicationContext());

        signInButton = findViewById(R.id.sign_in_btgoogle);
        signInButton.setOnClickListener(this);

        login = findViewById(R.id.login);
        login.setOnClickListener(this);

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        loginFacebook = findViewById(R.id.login_facebook);
        loginFacebook.setReadPermissions("email", "public_profile");

        logFacebook.loginTypeCheck();


    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            checkUserType(); //And login
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(login)) {
            if (isFormDisc.isFormFilled())
               logDisc.loginTypeCheck();
            else
                Toast.makeText(getApplicationContext(), "Please fill the form", Toast.LENGTH_LONG).show();
        } else if (view.equals(signInButton)) {

            logGoogle.signIn();
        } else if (view.equals(register)) {
            Intent registerActivity = new Intent(this, RegisterActivity.class);
            startActivity(registerActivity);
        }
    }





    //After a successful login start the main activity
    public void loginSuccessful(String userType, String userId) {
        Toast.makeText(getApplicationContext(), userType, Toast.LENGTH_LONG).show();

        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
        mainActivity.putExtra("USER_TYPE", userType);
        mainActivity.putExtra("USER_ID", userId);
        startActivity(mainActivity);
    }

    public void checkUserType() {
        user = mAuth.getCurrentUser();
        mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userType = null;
                String userId = user.getUid().toString();
                String dbUID;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    dbUID = child.child("ownerUID").getValue().toString();
                    if (dbUID.equals(userId)) {
                        userType = "owner";
                        break;
                    } else {
                        userType = "user";
                        break;
                    }
                }
                loginSuccessful(userType, userId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}