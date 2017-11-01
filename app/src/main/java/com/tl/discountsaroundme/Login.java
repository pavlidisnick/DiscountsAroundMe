package com.tl.discountsaroundme;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.tl.discountsaroundme.Activities.MainActivity;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Login extends Activity implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference("/shops");
    private Button login;



    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(login)) {
            if (isFormFilled()) {
                signIn(email, password);

            } else {
                Context context = getApplicationContext();
                CharSequence text = "Please fill the form";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information

                    CheckUserType();
                    Log.d(TAG, "signInWithEmail:success");

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
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
     * Checks if all fields are filled and the user is ready to be created
     */
    private boolean isFormFilled() {
        TextView email = findViewById(R.id.emailText);
        TextView password = findViewById(R.id.passwordText);
        this.email = email.getText().toString();
        this.password = password.getText().toString();

        return !this.email.isEmpty() && !this.password.isEmpty();
    }

    //After a successful login  start the main activity
    private void LoginSuccessful(String Type) {
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

    /**
     * CHECK  if the user is an owner or a customer by retrieving all the UIDs from  stores and comparing them to the user trying to login.
     */

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
                LoginSuccessful(usertype);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
