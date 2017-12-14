package com.tl.discountsaroundme.activities;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class RegisterActivity extends Activity implements View.OnClickListener, AnimCheckBox.OnCheckedChangeListener, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    GoogleMap map;
    double latitude=0;
    double longitude=0;
    private Button register;
    private Button login;
    private MapView tagShop;
    //Issue 8 params
    private DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();
    private AnimCheckBox cbBusinessAccount;
    private EditText etShopName;
    private Spinner sShopType;

    private String email;
    private String password;

    @Override
    protected void onResume() {
        super.onResume();
        tagShop.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tagShop.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tagShop.onPause();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        tagShop.onLowMemory();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);





        register = findViewById(R.id.register_button);
        login = findViewById(R.id.login_button);
        etShopName = findViewById(R.id.etShopName);

        tagShop = findViewById(R.id.tagShopOnMap);
        tagShop.onCreate(savedInstanceState);

        tagShop.getMapAsync(this);




        cbBusinessAccount = findViewById(R.id.cbBusinessAccount);
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
            Intent LoginActivity = new Intent(this, com.tl.discountsaroundme.activities.LoginActivity.class);
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
                    if (cbBusinessAccount.isChecked()) {
                        OnBusinessAccountCreation(task);
                    }
                    StoreAccountIntoDB(task);
                    // Sign in success, update UI with the signed-in user's information and store the user in the database
                    text = "RegisterActivity successful! Welcome " + task.getResult().getUser().getEmail();
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    // If sign in fails, display a message to the user.
                    text = "RegisterActivity Failed";
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
        //Issue 8 changes need when the business account cb is checked
        AnimCheckBox cbBusinessAccount = findViewById(R.id.cbBusinessAccount);
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
        NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Issue 8
     * On creation of a business account the Shop is stored into the database with its details and the account owner's UID
     * Currently the location of the shop is on default value
     * TODO Give the  owner the option to set the shop's location
     */

    public void OnBusinessAccountCreation(Task<AuthResult> task) {
        FirebaseUser user = task.getResult().getUser();
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

    public void StoreAccountIntoDB(Task<AuthResult> task) {
        FirebaseUser user = task.getResult().getUser();
        String userType = "Customer";
        if (cbBusinessAccount.isChecked()) {
            userType = "Store owner";
        }
        User newUser = new User(user.getEmail(), user.getEmail(), userType, "imgURL");
        mDbRef.child("users").child(user.getUid()).setValue(newUser);
    }

    /**
     * Issue 8
     * If user Checks the box more options become visible
     */
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

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.setOnMapClickListener(this);
        map.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {

        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//         longitude = location.getLongitude();
//         latitude = location.getLatitude();

        Toast.makeText(getApplicationContext(), latLng.toString(),
                Toast.LENGTH_LONG).show();


    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(latLng.toString())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        longitude = location.getLongitude();
//        latitude = location.getLatitude();

        Toast.makeText(getApplicationContext(),
                "New marker added@" + latLng.toString(), Toast.LENGTH_LONG)
                .show();
    }
}