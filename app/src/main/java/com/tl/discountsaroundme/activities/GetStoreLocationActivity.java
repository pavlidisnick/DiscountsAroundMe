package com.tl.discountsaroundme.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tl.discountsaroundme.R;

public class GetStoreLocationActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnCircleClickListener, GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener, View.OnClickListener {

    private GoogleMap mMap;

    private MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button confirmButton = findViewById(R.id.confirm);
        confirmButton.setOnClickListener(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setPadding(0, getPixels(), 0, 0);
    }

    private int getPixels() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, metrics);
    }

    @Override
    public void onCircleClick(Circle circle) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();

        markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(markerOptions);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
    }

    // TODO: add a dialog to confirm the location
    @Override
    public void onClick(View v) {
        if (markerOptions != null) {
            LatLng latLng = markerOptions.getPosition();

            Double latitude = latLng.latitude;
            Double longitude = latLng.longitude;

            Intent intent = new Intent();
            intent.putExtra("lat", latitude);
            intent.putExtra("lng", longitude);
            setResult(RESULT_OK, intent);

            finish();
        } else
            Toast.makeText(getApplicationContext(), "Click on your shop's location", Toast.LENGTH_SHORT).show();
    }
}
