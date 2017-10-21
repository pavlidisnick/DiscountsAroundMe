package com.tl.discountsaroundme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;

public class Register extends Activity implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Button register;
    Button login;

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.register_button);
        login = findViewById(R.id.login_button);

        register.setOnClickListener(this);
        login.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.equals(register)) {
            if (isFormFilled())
                signUp(email, password);
        }
        else if (view.equals(login)) {
//            Intent LoginActivity = new Intent(this, Login.class);
//            startActivity(LoginActivity);
        }
    }

    void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                }
                else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                }
            }
        });
    }


    /**
     * Checks if all fields are filled and the user is ready to be created
     */
    private boolean isFormFilled() {
        TextView email = findViewById(R.id.email);
        TextView password = findViewById(R.id.password);
        this.email = email.getText().toString();
        this.password = password.getText().toString();

        return !this.email.isEmpty() && !this.password.isEmpty();
    }

    /**
     * Checks if there is internet connection
     * @return true if it has internet connection
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}