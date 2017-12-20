package com.tl.discountsaroundme.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tl.discountsaroundme.R;

public class ChooseLocationStore extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCircleClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    double latitude=0;
    double longitude=0;
    String latLongSendToData;
    Intent intent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location_store);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onCircleClick(Circle circle) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//         longitude = location.getLongitude();
//         latitude = location.getLatitude();

        Toast.makeText(getApplicationContext(), latLng.toString(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        mMap.addMarker(new MarkerOptions()
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
        String[] latLong;
        String[] latReal;
        String[] longReal;
        latLong = latLng.toString().split(",");
        latReal = latLong[0].split("\\(");
        longReal = latLong[1].split("\\)");

        latitude = Double.parseDouble(latReal[1]);
        longitude = Double.parseDouble(longReal[0]);
        latLongSendToData = String.valueOf(latitude)+","+String.valueOf(longitude);
        intent.putExtra("data",latLongSendToData);
        setResult(RESULT_OK,intent);
        finish();

    }


}
