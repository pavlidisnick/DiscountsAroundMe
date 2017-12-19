package com.tl.discountsaroundme.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.ui_controllers.StatusBar;

public class LoginActivity extends FragmentActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, FacebookCallback<LoginResult> {
    private static final int RC_SIGN_IN = 9000;

    ProgressDialog progress;

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

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ProgressBar init
        progress = new ProgressDialog(this);
        progress.setMessage("Signing in");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        new StatusBar(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = findViewById(R.id.sign_in_btgoogle);
        signInButton.setOnClickListener(this);

        login = findViewById(R.id.login);
        login.setOnClickListener(this);

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        loginFacebook = findViewById(R.id.login_facebook);
        loginFacebook.setReadPermissions("email", "public_profile");

        //Facebook login
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        loginFacebook.registerCallback(callbackManager, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            showProgressBar();
            checkUserType();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(login)) {
            if (isFormFilled()) {
                showProgressBar();
                signIn(email, password);
            } else
                Toast.makeText(getApplicationContext(), "Please fill the form", Toast.LENGTH_SHORT).show();
        } else if (view.equals(signInButton)) {
            showProgressBar();
            signIn();
        } else if (view.equals(register)) {
            Intent registerActivity = new Intent(this, RegisterActivity.class);
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
                    checkUserType();
                } else {
                    Toast.makeText(getApplicationContext(), "LoginActivity failed", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            loginSuccessful("user", account != null ? account.getId() : null);
        } else {
            Toast.makeText(getApplicationContext(), "LoginActivity failed", Toast.LENGTH_SHORT).show();
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
    private void loginSuccessful(String userType, String userId) {
        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
        mainActivity.putExtra("USER_TYPE", userType);
        mainActivity.putExtra("USER_ID", userId);
        startActivity(mainActivity);

        hideProgressBar();
    }

    private void checkUserType() throws NullPointerException {
        user = mAuth.getCurrentUser();
        mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userType = null;
                String userId = user.getUid();
                String dbUID;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    dbUID = (String) child.child("ownerUID").getValue();
                    if (dbUID != null && dbUID.equals(userId)) {
                        userType = "owner";
                        break;
                    } else {
                        userType = "user";
                    }
                }
                loginSuccessful(userType, userId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showProgressBar() {
        progress.show();
    }

    private void hideProgressBar() {
        progress.dismiss();
    }

    // Facebook Callbacks
    @Override
    public void onSuccess(LoginResult loginResult) {
        user = mAuth.getCurrentUser();
        loginSuccessful("user", loginResult.getAccessToken().getUserId());
    }

    @Override
    public void onCancel() {
        Toast.makeText(getApplicationContext(), "Login cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(FacebookException e) {
        Toast.makeText(getApplicationContext(), "Facebook login failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}
