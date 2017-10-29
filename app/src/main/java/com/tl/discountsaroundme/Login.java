package com.tl.discountsaroundme;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;

public class Login extends Activity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 9000;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private GoogleApiClient mGoogleApiClient ;
    Button login;
    LoginButton loginfacebook;
    CallbackManager callbackManager;
    SignInButton signInButton;
    String email;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
      GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

// Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
      //  mGoogleApiClient = new GoogleApiClient.Builder(this)
        //        .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
          //      .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            //    .build();




        //signInButton.setSize(SignInButton.SIZE_STANDARD);
        //signInButton.setOnClickListener(this);
        signInButton= (SignInButton) findViewById(R.id.sign_in_btgoogle);

        //Facebook login
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        login.setOnClickListener(this);


        loginfacebook=findViewById(R.id.login_facebook);
        loginfacebook.setReadPermissions("email","public_profile");
        callbackManager = CallbackManager.Factory.create();
        loginfacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final String UserId = loginResult.getAccessToken().getUserId();
/*)                final GraphRequest graphRequest = GraphRequest.newGraphPathRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                displayUserInfo(object);
                            }
                        });
*/


                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                displayUserInfo(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();


                Toast.makeText(getApplicationContext(), "Login Successful\n", Toast.LENGTH_LONG).show();
            }



            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login Cancelled\n", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "####Error-facebook####\n", Toast.LENGTH_LONG).show();
            }
        });
    }






    @Override
    public void onClick(View view) {
        if (view.equals(login)) {
            if (isFormFilled()) {
                signIn(email, password);

            }
          /*  else if(view.equals(R.id.sign_in_btgoogle)){
                signIn();
            }*/
            else{
                Context context = getApplicationContext();
                CharSequence text = "Please fill the form";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context,text,duration);
                toast.show();
            }

        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    user = mAuth.getCurrentUser();


                    Context context = getApplicationContext();
                    CharSequence text = "Login successful";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context,text,duration);
                    toast.show();
                    String Username = user.getEmail();
                    LoginSuccessful(Username);

                }
                else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Context context = getApplicationContext();
                    CharSequence text = "Login fail.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context,text,duration);
                    toast.show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(getApplicationContext(), "Login Successful\n", Toast.LENGTH_LONG).show();
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getApplicationContext(), "Login failed\n", Toast.LENGTH_LONG).show();
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
    //After a successful login ,Pass the username , and start the main activity
    private void LoginSuccessful (String Username){
        Intent MainActivity = new Intent(this, com.tl.discountsaroundme.Activities.MainActivity.class);
        MainActivity.putExtra("Username", Username);
        startActivity(MainActivity);
    }
    public void displayUserInfo(JSONObject object){
        String first_name, last_name , email,id,name;
        try {
            first_name = object.getString("first_name");
            last_name = object.getString("last_name");
            email = object.getString("email");
            id = object.getString("id");
            name = object.getString("name");
            LoginSuccessful(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
