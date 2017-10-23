package com.tl.discountsaroundme;

import android.Manifest;
import android.content.pm.PackageManager;
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
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    LocationManager locationManager;
    Button nearbyButton;
    GPSTracker gps;
    GoogleMap gm;

    LatLng bershkaSerres = new LatLng(41.090099,23.5485317);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gm = googleMap;
        nearbyButton = findViewById(R.id.nearbyButton);

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
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        gps = new GPSTracker(locationManager);

        if (gps.canGetLocation()) {
            lat = gps.getLatitude();
            lng = gps.getLongitude();
        }
        gps.stopUsingGPS();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
        googleMap.animateCamera(zoom);

        nearbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double distance = measure(bershkaSerres.latitude, bershkaSerres.longitude, gps.getLatitude(), gps.getLongitude());

                if (distance <= 100)
                    gm.addMarker(new MarkerOptions().position(bershkaSerres).title("Bershka"));
                else
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