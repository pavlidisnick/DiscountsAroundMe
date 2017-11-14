package com.tl.discountsaroundme.Fragments;


import android.content.Context;
import android.location.LocationManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tl.discountsaroundme.Discounts.SearchSuggest;
import com.tl.discountsaroundme.Discounts.SuggestListMaker;
import com.tl.discountsaroundme.Entities.Store;
import com.tl.discountsaroundme.FirebaseData.StoreManager;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.Services.GPSTracker;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MapTab extends Fragment {
    private MapView mMapView;
    private GPSTracker gps;
    private GoogleMap googleMap;
    private StoreManager storeManager = new StoreManager();
    private double distance = 1; // in km

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_map, container, false);

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
                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);
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
                ArrayList<Store> stores = storeManager.getNearbyStores(gps.getLatitude(), gps.getLongitude(), distance * 1000);
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
                // TODO: offer value needs implementation
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

        final LinearLayout popupMenu = rootView.findViewById(R.id.popup_menu);

        ImageButton closePopup = rootView.findViewById(R.id.close_popup);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.setVisibility(View.INVISIBLE);
            }
        });

        final FloatingSearchView mSearchView = rootView.findViewById(R.id.map_floating_search);
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.gps_fixed) {
                    LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 16.29));
                } else if (itemId == R.id.map_options) {
                    int visibility = popupMenu.getVisibility();
                    if (visibility == View.VISIBLE)
                        popupMenu.setVisibility(View.INVISIBLE);
                    else
                        popupMenu.setVisibility(View.VISIBLE);
                }
            }
        });

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                SuggestListMaker suggestListMaker = new SuggestListMaker();
                List<SearchSuggest> searchSuggestList;
                searchSuggestList = suggestListMaker.convertStringsToSuggestions(storeManager.getStoreStrings(), newQuery);

                mSearchView.swapSuggestions(searchSuggestList);
            }
        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, final TextView textView, final SearchSuggestion item, int itemPosition) {
                suggestionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Store store = storeManager.getStoreFromName(item.getBody());
                        if (store != null) {
                            LatLng latLng = new LatLng(store.getLat(), store.getLng());
                            MarkerOptions marker = new MarkerOptions()
                                    .position(latLng)
                                    .title(store.getName())
                                    .snippet(store.getType())
                                    .flat(true)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_coffee));
                            googleMap.addMarker(marker);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 16.29));
                        }
                        mSearchView.setSearchBarTitle(item.getBody());
                        mSearchView.clearFocus();
                    }
                });
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
