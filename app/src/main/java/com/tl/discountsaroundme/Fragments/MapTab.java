package com.tl.discountsaroundme.Fragments;


import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tl.discountsaroundme.Entities.Store;
import com.tl.discountsaroundme.FirebaseData.StoreManager;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.Services.GPSTracker;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class MapTab extends Fragment {
    private MapView mMapView;
    private GPSTracker gps;
    private GoogleMap googleMap;
    private StoreManager storeManager = new StoreManager();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_map, container, false);

        mMapView = rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                try {
                    googleMap.setMyLocationEnabled(true);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });

        Button shopsButton = rootView.findViewById(R.id.shopsButton);
        Button nearbyButton = rootView.findViewById(R.id.nearbyButton);

        AtomicReference<LocationManager> locationManager;
        locationManager = new AtomicReference<>((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE));
        gps = new GPSTracker(locationManager.get());

        shopsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.clear();
                for (Store store : storeManager.getStores()) {
                    MarkerOptions marker = new MarkerOptions()
                            .position(new LatLng(store.getLat(), store.getLng()))
                            .title(store.getName())
                            .snippet(store.getType())
                            .flat(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_coffee));
                    googleMap.addMarker(marker);
                }
            }
        });

        nearbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.clear();
                ArrayList<Store> stores = storeManager.getNearbyStores(gps.getLatitude(), gps.getLongitude(), 100);
                if (stores.isEmpty())
                    Toast.makeText(getContext(), "There are no shops nearby", Toast.LENGTH_LONG).show();
                else {
                    for (Store store : stores) {
                        MarkerOptions marker = new MarkerOptions()
                                .position(new LatLng(store.getLat(), store.getLng()))
                                .title(store.getName())
                                .snippet(store.getType())
                                .flat(true)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_coffee));
                        googleMap.addMarker(marker);
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
