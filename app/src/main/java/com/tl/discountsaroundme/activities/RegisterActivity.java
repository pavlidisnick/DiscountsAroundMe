package com.tl.discountsaroundme.activities;

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
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.entities.Store;
import com.tl.discountsaroundme.entities.User;
import com.tl.discountsaroundme.firebase_data.StoreManager;
import com.tl.discountsaroundme.firebase_data.UserInfoManager;

public class RegisterActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Button register;
    private Button login;
    private final DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();
    private CheckBox cbBusinessAccount;
    private EditText etShopName;
    private TextView tvShopLocation;
    private Spinner sShopType;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.register_button);
        login = findViewById(R.id.login_button);
        etShopName = findViewById(R.id.etShopName);
        cbBusinessAccount = findViewById(R.id.cbBusinessAccount);
        tvShopLocation = findViewById(R.id.tvShopLocation);
        sShopType = findViewById(R.id.sShopType);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.shopTypeSpinner, R.layout.spinner_dropdown_list);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        sShopType.setAdapter(spinnerAdapter);
        cbBusinessAccount.setOnCheckedChangeListener(this);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOnlineStatus();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                if (isFormFilled()) {
                    signUp(email, password);
                } else {
                    toastMessage("Please fill the form");
                }
                break;
            case R.id.login:
                Intent LoginActivity = new Intent(this, com.tl.discountsaroundme.activities.LoginActivity.class);
                startActivity(LoginActivity);
                break;
        }
    }

    /**
     * On a Successful register Store the user inside the database.
     * if the user is a Shop owner then also store the shop into the database, the key
     * of the store is the user's UID
     */
    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (cbBusinessAccount.isChecked()) {
                        StoreManager.StoreShopOnDb(
                                mDbRef,
                                task.getResult().getUser(),
                                etShopName.getText().toString(),
                                sShopType.getSelectedItem().toString());
                    }
                    UserInfoManager userInfoManager = new UserInfoManager(mAuth.getCurrentUser(), mDbRef);
                    userInfoManager.StoreUserToDB(cbBusinessAccount.isChecked());
                } else {
                    toastMessage("Register Failed!");
                }
            }
        });
    }

    private void checkOnlineStatus() {
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

    /**
     * Checks if all fields are filled and the user is ready to be created
     */
    private boolean isFormFilled() {
        CheckBox cbBusinessAccount = findViewById(R.id.cbBusinessAccount);
        EditText etShopName = findViewById(R.id.etShopName);
        Spinner sShopType = findViewById(R.id.sShopType);
        TextView email = findViewById(R.id.email);
        TextView password = findViewById(R.id.password);
        if (cbBusinessAccount.isChecked()) {
            String shopName = etShopName.getText().toString();
            this.email = email.getText().toString();
            this.password = password.getText().toString();
            Boolean itemSelected = sShopType.getSelectedItemPosition() != 0;
            return !this.email.isEmpty() && !this.password.isEmpty() && !shopName.isEmpty() && itemSelected;
        } else {
            this.email = email.getText().toString();
            this.password = password.getText().toString();
            return !this.email.isEmpty() && !this.password.isEmpty();
        }
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            etShopName.setVisibility(View.VISIBLE);
            sShopType.setVisibility(View.VISIBLE);
            tvShopLocation.setVisibility(View.VISIBLE);
        } else {
            etShopName.setVisibility(View.GONE);
            sShopType.setVisibility(View.GONE);
            tvShopLocation.setVisibility(View.GONE);
        }
    }

    private void toastMessage(String Message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, Message, duration);
        toast.show();
    }
}