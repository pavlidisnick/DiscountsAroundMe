package com.tl.discountsaroundme.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.tl.discountsaroundme.ui_controllers.AnimCheckBox;
import com.tl.discountsaroundme.ui_controllers.StatusBar;

public class RegisterActivity extends Activity implements View.OnClickListener, AnimCheckBox.OnCheckedChangeListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final static int REQ_CODE = 1;

    double latitude = Double.NaN;
    double longitude = Double.NaN;
    private Button register;
    private Button login;
    private Button tagShop;
    //Issue 8 params
    private DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();
    private AnimCheckBox cbBusinessAccount;
    private EditText etShopName;
    private Spinner sShopType;

    private String email;
    private String password;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        new StatusBar(this);

        progress = new ProgressDialog(this);
        progress.setMessage("Signing in");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        register = findViewById(R.id.register_button);
        login = findViewById(R.id.login_button);
        etShopName = findViewById(R.id.etShopName);

        tagShop = findViewById(R.id.tagShopOnMap);
        tagShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseStoreLocation = new Intent(getApplicationContext(), GetStoreLocationActivity.class);
                startActivityForResult(chooseStoreLocation, REQ_CODE);
            }
        });

        cbBusinessAccount = findViewById(R.id.cbBusinessAccount);

        sShopType = findViewById(R.id.sShopType);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
                .createFromResource(this, R.array.shopTypeSpinner, R.layout.spinner_dropdown_list);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        sShopType.setAdapter(spinnerAdapter);

        cbBusinessAccount.setOnCheckedChangeListener(this);

        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            latitude = data.getDoubleExtra("lat", 0);
            longitude = data.getDoubleExtra("lng", 0);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(register)) {
            if (isFormFilled()) {
                signUp(email, password);
                progress.show();
            } else
                toast("Please fill all the fields");
        } else if (view.equals(login)) {
            this.finish();
        }
    }

    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userType = "user";
                    FirebaseUser user = task.getResult().getUser();

                    if (cbBusinessAccount.isChecked()) {
                        userType = "owner";
                        onBusinessAccountCreation(user);
                    }
                    storeAccountIntoDB(user, userType);

                    toast("Registered successfully");

                    goToMain(userType, user.getUid());
                } else {
                    toast("Register failed");
                }
                progress.dismiss();
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

        boolean isFilled = !this.email.isEmpty() && !this.password.isEmpty();

        if (cbBusinessAccount.isChecked()) {
            String shopName = etShopName.getText().toString();
            String shopType = sShopType.getSelectedItem().toString();

            boolean isBusinessFormFilled = !shopName.isEmpty() && !shopType.equals("Choose a type");
            boolean locationCheck = !Double.isNaN(latitude) && !Double.isNaN(longitude);

            return isBusinessFormFilled && locationCheck && isFilled;
        }

        return isFilled;
    }

    /**
     * Issue 8
     * On creation of a business account the Shop is stored into the database with its details and the account owner's UID
     * Currently the location of the shop is on default value
     */
    private void onBusinessAccountCreation(FirebaseUser user) {
        String BAUserUID = user.getUid();

        Store Shop = new Store();
        Shop.setDescription("Details");
        Shop.setName(etShopName.getText().toString());
        Shop.setImage("");
        Shop.setLat(latitude);
        Shop.setLng(longitude);
        Shop.setType(sShopType.getSelectedItem().toString());
        Shop.setOwnerUID(BAUserUID);
        // Now Shops are stored under their owner's UID
        mDbRef.child("shops").child(user.getUid()).setValue(Shop);
    }

    private void storeAccountIntoDB(FirebaseUser user, String userType) {
        User newUser = new User(user.getEmail(), user.getEmail(), userType, "imgURL");
        mDbRef.child("users").child(user.getUid()).setValue(newUser);
    }

    @Override
    public void onChange(AnimCheckBox view, boolean checked) {
        if (checked) {
            etShopName.setVisibility(View.VISIBLE);
            sShopType.setVisibility(View.VISIBLE);
            tagShop.setVisibility(View.VISIBLE);
        } else {
            etShopName.setVisibility(View.GONE);
            sShopType.setVisibility(View.GONE);
            tagShop.setVisibility(View.GONE);
        }
    }

    private void toast(String display) {
        Toast.makeText(getApplicationContext(), display, Toast.LENGTH_SHORT).show();
    }

    private void goToMain(String userType, String userId) {
        Intent mainActivity = new Intent(RegisterActivity.this, MainActivity.class);
        mainActivity.putExtra("USER_TYPE", userType);
        mainActivity.putExtra("USER_ID", userId);
        startActivity(mainActivity);
        finish();
    }
}