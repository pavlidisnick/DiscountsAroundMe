package com.tl.discountsaroundme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.FirebaseDatabase;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.entities.Store;
import com.tl.discountsaroundme.firebase_data.DiscountsManager;
import com.tl.discountsaroundme.firebase_data.StoreManager;
import com.tl.discountsaroundme.map.MarkerHelper;
import com.tl.discountsaroundme.map.SetSearchBar;
import com.tl.discountsaroundme.services.GPSTracker;

import java.util.ArrayList;


public class MapTab extends Fragment {
    public static double distance = 1; // in km
    private MapView mMapView;
    private GPSTracker gps;
    private GoogleMap googleMap;
    private MarkerHelper markerHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_map, container, false);

        final StoreManager storeManager = new StoreManager();

        DiscountsManager discountsManager = new DiscountsManager();
        discountsManager.showTopDiscounts(FirebaseDatabase.getInstance(), DiscountsTab.discountValue);

        mMapView = rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        final FloatingSearchView mSearchView = rootView.findViewById(R.id.map_floating_search);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                markerHelper = new MarkerHelper(MapTab.this, googleMap);
                SetSearchBar setSearchBar = new SetSearchBar(mSearchView, storeManager, markerHelper);
                mSearchView.setOnQueryChangeListener(setSearchBar);
                mSearchView.setOnBindSuggestionCallback(setSearchBar);

                try {
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });

//        AtomicReference<LocationManager> locationManager;
//        locationManager = new AtomicReference<>((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE));

        gps = new GPSTracker(getActivity(), storeManager, discountsManager, googleMap);

        Button shopsButton = rootView.findViewById(R.id.shopsButton);
        Button nearbyButton = rootView.findViewById(R.id.nearbyButton);

        shopsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Store> stores = storeManager.getStores();
                markerHelper.addMarkersFromList(stores);
            }
        });

        nearbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Store> stores = new ArrayList<>();
                try {
                    stores.addAll(storeManager.getNearbyStores(gps.getLatitude(), gps.getLongitude(), distance * 1000));
                } catch (NullPointerException e) {
                    Toast.makeText(getContext(), "GPS disabled", Toast.LENGTH_SHORT).show();
                }

                if (stores.isEmpty())
                    Toast.makeText(getContext(), "There are no shops nearby", Toast.LENGTH_SHORT).show();
                else
                    markerHelper.addMarkersFromList(stores);
            }
        });

        SeekBar radiusSeekBar = rootView.findViewById(R.id.radius_seekBar);
        final TextView radiusTextView = rootView.findViewById(R.id.radius_textView);
        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 1) {
                    String displayText = "Shop Radius: <1 km";
                    radiusTextView.setText(displayText);
                    distance = 0.5;
                } else {
                    String displayText = "Shop Radius: " + progress + "km";
                    radiusTextView.setText(displayText);
                    distance = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final SeekBar offersSeekBar = rootView.findViewById(R.id.offer_seekBar);
        final TextView offersTextView = rootView.findViewById(R.id.offers_textView);
        offersSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DiscountsTab.discountValue = progress;
                String displayText = "Offers above " + progress + "0%";
                offersTextView.setText(displayText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        CheckBox topOffersCheck = rootView.findViewById(R.id.topOffers_check);
        topOffersCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                    offersSeekBar.setEnabled(false);
                else
                    offersSeekBar.setEnabled(true);
            }
        });

        CheckBox nearbyOffersCheck = rootView.findViewById(R.id.nearbyOffers_check);
        nearbyOffersCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gps.toggleNotifications(isChecked);
            }
        });

        final LinearLayout popupMenu = rootView.findViewById(R.id.popup_menu);

        ImageButton closePopup = rootView.findViewById(R.id.close_popup);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.setVisibility(View.INVISIBLE);
            }
        });

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.gps_fixed) {
                    try {
                        gps.getLocation();
                        LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 16.29));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "GPS disabled", Toast.LENGTH_LONG).show();
                    }
                } else if (itemId == R.id.map_options) {
                    int visibility = (popupMenu.getVisibility() == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE;
                    popupMenu.setVisibility(visibility);
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

    @Override
    public void onStop() {
        super.onStop();
    }
}
