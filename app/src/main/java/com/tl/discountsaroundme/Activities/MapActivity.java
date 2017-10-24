package com.tl.discountsaroundme.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.Entities.Store;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.Services.GPSTracker;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private static ArrayList<Store> stores = new ArrayList<>();
    private com.tl.discountsaroundme.Services.GPSTracker gps;
    private GoogleMap gm;

    private DatabaseReference databaseStores = FirebaseDatabase.getInstance().getReference("Stores");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        getSystemService(LOCATION_SERVICE);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gm = googleMap;
        Button nearbyButton = findViewById(R.id.nearbyButton);
        Button shopsButton = findViewById(R.id.shopsButton);

        databaseStores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot storesSnapshot: dataSnapshot.getChildren()) {
                    try {
                        Store store = storesSnapshot.getValue(Store.class);
                        stores.add(store);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        try {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.girly_style));
        }
        catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        googleMap.setMyLocationEnabled(true);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        double lat = 0;
        double lng = 0;
        AtomicReference<LocationManager> locationManager;
        locationManager = new AtomicReference<>((LocationManager) this.getSystemService(LOCATION_SERVICE));
        gps = new GPSTracker(locationManager.get());

        if (gps.canGetLocation()) {
            lat = gps.getLatitude();
            lng = gps.getLongitude();
        }
        gps.stopUsingGPS();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
        googleMap.animateCamera(zoom);

        shopsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gm.clear();
                for (Store store: stores) {
                    gm.addMarker(new MarkerOptions().position(new LatLng(store.getLat(), store.getLng())).title(store.getName()));
                }
            }
        });


        nearbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gm.clear();
                boolean noStores = true;

                for (Store store: stores) {
                    LatLng latLng = new LatLng(store.getLat(), store.getLng());
                    double distance = measure(latLng.latitude, latLng.longitude, gps.getLatitude(), gps.getLongitude());
                    if (distance <= 100) {
                        gm.addMarker(new MarkerOptions().position(latLng).title(store.getName()));
                        noStores = false;
                    }
                }
                if (noStores)
                    Toast.makeText(getApplicationContext(), "There are no shops nearby", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Measures the distance between 2 locations in meters
     * @param lat1 first location latitude
     * @param lng1 first location longitude
     * @param lat2 second location latitude
     * @param lng2 second location longitude
     * @return the distance in meters
     */
    public double measure(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (float) (earthRadius * c);
    }
}