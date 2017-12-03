package com.tl.discountsaroundme.activities.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tl.discountsaroundme.activities.LoginActivity;

import static com.tl.discountsaroundme.activities.LoginActivity.RC_SIGN_IN;


public class LoginGoogle extends FragmentActivity implements ProgramLogin,GoogleApiClient.OnConnectionFailedListener {
CallbackManager callbackManager;
GoogleApiClient mGoogleApiClient;

    public void loginTypeCheck() {



    }
    /**
     * Google signIn
     */
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
            Toast.makeText(getApplicationContext(), "LoginActivity Successful", Toast.LENGTH_LONG).show();
            GoogleSignInAccount account = result.getSignInAccount();
            LoginActivity log =new LoginActivity();
                    log.loginSuccessful("user", account.getId());
        } else {
            Toast.makeText(getApplicationContext(), "LoginActivity failed", Toast.LENGTH_LONG).show();
        }
    }



}

