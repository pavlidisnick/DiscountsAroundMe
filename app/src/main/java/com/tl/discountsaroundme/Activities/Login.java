package com.tl.discountsaroundme.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.FirebaseData.UserInfoManager;
import com.tl.discountsaroundme.Fragments.UserTab;
import com.tl.discountsaroundme.R;

public class Login extends FragmentActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 9000;

    Button login;
    Button register;
    LoginButton loginFacebook;
    SignInButton signInButton;
    String email;
    String password;

    CallbackManager callbackManager;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference("/shops");
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(isLoggedIn()==true || (mGoogleApiClient != null && mGoogleApiClient.isConnected())) {
            Toast.makeText(getApplicationContext(), "Already connected", Toast.LENGTH_LONG).show();
            loginSuccessful("user");
        }else{

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

        callbackManager = CallbackManager.Factory.create();
        loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                loginSuccessful("user");
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login Cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "####Error-facebook####", Toast.LENGTH_LONG).show();
            }
        });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            CheckUserType(); //And login
        }
    }

    /**
     * Added a signout way thus the login tests will be able to run
     */

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (mSharedPreferences.getBoolean("logoutKey",false)){
            mAuth.signOut();
        }
    }


    @Override
    public void onClick(View view) {
        if (view.equals(login)) {
            if (isFormFilled())
                signIn(email, password);
            else {
                Toast.makeText(getApplicationContext(), "Please fill the form", Toast.LENGTH_LONG).show();
            }
        } else if (view.equals(signInButton)) {
            signIn();
        } else if (view.equals(register)) {
            Intent registerActivity = new Intent(this, Register.class);
            startActivity(registerActivity);
        }
    }

    /**
     * Google signIn
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Sign in with email and password
     */
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information

                    CheckUserType();


                } else {
                    // If sign in fails, display a message to the user.

                    Context context = getApplicationContext();
                    CharSequence text = "Login fail.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
    }

    /**
     * Checks result of google signIn
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Google signIn connection failure
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_LONG).show();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
            loginSuccessful("user");
        } else {
            Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Checks if all fields are filled and the user is ready to be created
     */
    private boolean isFormFilled() {
        TextView email = findViewById(R.id.emailText);
        TextView password = findViewById(R.id.passwordText);
        this.email = email.getText().toString();
        this.password = password.getText().toString();

        return !this.email.isEmpty() && !this.password.isEmpty();
    }

    //After a successful login start the main activity
    private void loginSuccessful(String Type) {

        //TODO: change the main activity according to the user TYPE
        CharSequence text= "";
        text = Type;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast;
        toast = Toast.makeText(context, text, duration);
        toast.show();

        Intent MainActivity = new Intent(Login.this, MainActivity.class);

        startActivity(MainActivity);
    }

    private void CheckUserType (){
        user = mAuth.getCurrentUser();
        String Username = user.getEmail();
        mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String usertype = null;
                String UserUID = user.getUid().toString();
                String dbUID;
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    dbUID = child.child("ownerUID").getValue().toString();
                    if (  dbUID.equals(UserUID)){
                        usertype="owner";
                    }else {usertype = "user";}
                }
                loginSuccessful(usertype);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken == null)
            return false;
        else
            return  true;
    }
}
