package com.tl.discountsaroundme.fragments;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.UserPreferences;
import com.tl.discountsaroundme.WeatherApi.WeatherApiCommon;
import com.tl.discountsaroundme.WeatherApi.WeatherTask;
import com.tl.discountsaroundme.entities.Store;
import com.tl.discountsaroundme.firebase_data.DiscountsManager;
import com.tl.discountsaroundme.firebase_data.StoreManager;
import com.tl.discountsaroundme.map.MarkerHelper;
import com.tl.discountsaroundme.map.NearbyStoreList;
import com.tl.discountsaroundme.map.SetSearchBar;
import com.tl.discountsaroundme.services.GPSTracker;

import java.util.ArrayList;
import java.util.Objects;


public class MapTab extends Fragment {
    public static double distance = 1;
    public static double notifyEvery = 30;
    public static boolean isNotificationsEnabled = true;

    private MapView mMapView;
    private GPSTracker gps;
    private GoogleMap googleMap;
    private MarkerHelper markerHelper;
  
    public  static CheckBox cbWeather;
    private CheckBox nearbyOffersCheck;
  
    private FrameLayout popupMenu;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_map, container, false);

        final StoreManager storeManager = new StoreManager();

        final DiscountsManager discountsManager = new DiscountsManager();
        discountsManager.fillListWithDiscounts(FirebaseDatabase.getInstance());

        new UserPreferences();

        cbWeather = rootView.findViewById(R.id.cbWeather);
        if (cbWeather.isChecked()) {
            new WeatherTask().execute(WeatherApiCommon.apiRequest(String.valueOf(gps.getLatitude()), String.valueOf(gps.getLongitude())));
        }
        mMapView = rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        final FloatingSearchView mSearchView = rootView.findViewById(R.id.map_floating_search);

        try {
            MapsInitializer.initialize(Objects.requireNonNull(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                markerHelper = new MarkerHelper(getActivity(), googleMap);
                SetSearchBar setSearchBar = new SetSearchBar(mSearchView, storeManager, markerHelper);
                mSearchView.setOnQueryChangeListener(setSearchBar);
                mSearchView.setOnBindSuggestionCallback(setSearchBar);

                NearbyStoreList nearbyStoreList = new NearbyStoreList(getActivity(), googleMap, storeManager);

                gps = new GPSTracker(getActivity(), storeManager, discountsManager, markerHelper, nearbyStoreList);

                try {
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.blue_style));
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });

        Button shopsButton = rootView.findViewById(R.id.shopsButton);
        Button nearbyButton = rootView.findViewById(R.id.nearbyButton);

        shopsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Store> stores = storeManager.getStores();
                markerHelper.addMarkersFromList(stores);
                hideMenu();
            }
        });

        nearbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Store> stores = new ArrayList<>();
                try {
                    stores.addAll(storeManager.getNearbyStores(gps.getLatitude(), gps.getLongitude(), distance));
                } catch (NullPointerException e) {
                    Toast.makeText(getContext(), "GPS disabled", Toast.LENGTH_SHORT).show();
                }

                if (stores.isEmpty())
                    Toast.makeText(getContext(), "There are no shops nearby", Toast.LENGTH_SHORT).show();
                else
                    markerHelper.addMarkersFromList(stores);
                hideMenu();
            }
        });

        final SeekBar radiusSeekBar = rootView.findViewById(R.id.radius_seekBar);
        final TextView radiusTextView = rootView.findViewById(R.id.radius_textView);

        // Get user's saved preferences  and set the text view ,progress bar and distance.
        radiusSeekBar.setProgress(UserPreferences.getDataInt("RadiusSeekBar"));
        String displayText = "Shop Radius: " + radiusSeekBar.getProgress() + "m";
        radiusTextView.setText(displayText);
        distance = radiusSeekBar.getProgress();

        // On progress bar changes save the new Values
        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                UserPreferences.saveDataInt("RadiusSeekBar", progress);
                String displayText = "Shop Radius: " + progress + "m";
                radiusTextView.setText(displayText);
                distance = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //Get User Preferences on Weather checked.
        cbWeather.setChecked(UserPreferences.getDataBool("WeatherCheck"));
        cbWeather.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserPreferences.saveDataBool("WeatherCheck", isChecked);
                if (isChecked) {
                    new WeatherTask().execute(WeatherApiCommon.apiRequest(String.valueOf(gps.getLatitude()), String.valueOf(gps.getLongitude())));
                }
            }

        });

        popupMenu = rootView.findViewById(R.id.popup_menu);

        ImageButton closePopup = rootView.findViewById(R.id.close_popup);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu();
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
                        Toast.makeText(getContext(), "GPS disabled", Toast.LENGTH_SHORT).show();
                    }
                } else if (itemId == R.id.map_options) {
                    if (popupMenu.getVisibility() == View.INVISIBLE)
                        showMenu();
                    else
                        hideMenu();
                }
            }
        });

        return rootView;
    }

    private void showMenu() {
        popupMenu.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.SlideInDown)
                .duration(400)
                .playOn(popupMenu);
    }

    private void hideMenu() {
        YoYo.with(Techniques.SlideOutUp)
                .duration(400)
                .onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        popupMenu.setVisibility(View.INVISIBLE);
                    }
                })
                .playOn(popupMenu);
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
