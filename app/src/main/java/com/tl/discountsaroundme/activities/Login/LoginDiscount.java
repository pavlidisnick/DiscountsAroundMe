package com.tl.discountsaroundme.activities.Login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.activities.LoginActivity;


public class LoginDiscount extends FragmentActivity implements ProgramLogin {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String email;
    String password;
    LoginActivity log = new LoginActivity();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public  void loginTypeCheck(){
        log = null;
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        log.checkUserType();
                    } else {
                        Toast.makeText(getApplicationContext(), "LoginActivity failed", Toast.LENGTH_LONG).show();
                    }
                }
            });

    }

    @Override
    public void signIn() {

    }

    public boolean isFormFilled() {
        TextView email = findViewById(R.id.emailText);
        TextView password = findViewById(R.id.passwordText);
        this.email = email.getText().toString();
        this.password = password.getText().toString();

        return !this.email.isEmpty() && !this.password.isEmpty();
    }



}
