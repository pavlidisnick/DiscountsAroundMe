package com.tl.discountsaroundme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tl.discountsaroundme.Entities.Store;

import static android.content.ContentValues.TAG;

public class Register extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    private Button register;
    private Button login;
    //Issue 8 params
    private DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();
    private CheckBox cbBusinessAccount;
    private EditText etShopName;
    private TextView tvShopLocation;
    private Spinner sShopType;

    private String email;
    private String password;
    private String ShopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.register_button);
        login = findViewById(R.id.login_button);
        etShopName = findViewById(R.id.etShopName);
        cbBusinessAccount =  findViewById(R.id.cbBusinessAccount);
        tvShopLocation = findViewById(R.id.tvShopLocation);
        sShopType = findViewById(R.id.sShopType);
        ArrayAdapter<CharSequence> spineradapter = ArrayAdapter.createFromResource(this,R.array.shopTypeSpinner,R.layout.spinnerdropdownlist);
        spineradapter.setDropDownViewResource(R.layout.spinnerdropdownlist);
        sShopType.setAdapter(spineradapter);

        cbBusinessAccount.setOnCheckedChangeListener(this);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isOnline()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No internet connection")
                    .setCancelable(false)
                    .setMessage("Please open wifi or mobile data.")
                    .setPositiveButton("Settings",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(i);
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(register)) {
            if (isFormFilled())
                signUp(email, password);
            else {
                Context context = getApplicationContext();
                CharSequence text = "Please fill the form";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        } else if (view.equals(login)) {
            Intent LoginActivity = new Intent(this, Login.class);
            startActivity(LoginActivity);
        }
    }

    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Context context = getApplicationContext();
                CharSequence text;
                int duration = Toast.LENGTH_SHORT;

                if (task.isSuccessful()) {
                    //Issue 8  in case the user is a shop owner
                    if (cbBusinessAccount.isChecked()){
                        OnBusinessAccountCreation(task);}
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    text = "Register successful! Welcome";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    text = "Register Failed";
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
        //Issue 8 changes need when the buisiness account cb is checked
        CheckBox cbBuisnessAccount = findViewById(R.id.cbBusinessAccount);
        EditText etShopName = findViewById(R.id.etShopName);
        Spinner  sShopType = findViewById(R.id.sShopType);

        TextView email = findViewById(R.id.email);
        TextView password = findViewById(R.id.password);

        if (cbBuisnessAccount.isChecked()){
           this.ShopName = etShopName.getText().toString();
           this.email = email.getText().toString();
           this.password = password.getText().toString();
           Boolean itemSelected = sShopType.getSelectedItemPosition() != 0;
           return !this.email.isEmpty() && !this.password.isEmpty() && !this.ShopName.isEmpty() && itemSelected  ;
        }else{
            this.email = email.getText().toString();
            this.password = password.getText().toString();

            return !this.email.isEmpty() && !this.password.isEmpty();}
    }

    /**
     * Checks if there is internet connection
     *
     * @return true if it has internet connection
     */
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Issue 8
     *If user Checks the box more options become visible
     */

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
           if (isChecked){
                etShopName.setVisibility(View.VISIBLE);
                sShopType.setVisibility(View.VISIBLE);
                tvShopLocation.setVisibility(View.VISIBLE);
            }else{
                etShopName.setVisibility(View.GONE);
                sShopType.setVisibility(View.GONE);
                tvShopLocation.setVisibility(View.GONE);
            }
        }

    /**
     * Issue 8
     * On creation of a business account the Shop is stored into the database with its details and the account owner's UID
     * Currently the location of the shop is on default value
     * TODO Give the  owner the option to set the shop's location
     * */

    public void OnBusinessAccountCreation(Task<AuthResult> task){

        FirebaseUser user = task.getResult().getUser();
        String BAuserUID = user.getUid();
        Store Shop = new Store();

        Shop.setDescription("Details");
        Shop.setName(etShopName.getText().toString());
        Shop.setImage("");
        Shop.setLat(0);
        Shop.setLng(0);
        Shop.setType(sShopType.getSelectedItem().toString());
        Shop.setOwnerUID(BAuserUID);

        String Key = mDbRef.child("shops").push().getKey();
        mDbRef.child("shops").child(Key).setValue(Shop);
    }
}