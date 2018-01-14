package com.tl.discountsaroundme.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.entities.Store;
import com.tl.discountsaroundme.firebase_data.StoreManager;
import com.tl.discountsaroundme.ui_controllers.GlideApp;

import java.util.ArrayList;

public class NearbyStoreList {
    private Activity activity;
    private GoogleMap googleMap;
    private StoreManager storeManager;
    private MarkerHelper markerHelper;
    private LinearLayout linearLayout;

    public NearbyStoreList(Activity activity, GoogleMap googleMap, StoreManager storeManager) {
        this.activity = activity;
        this.googleMap = googleMap;
        this.storeManager = storeManager;
        markerHelper = new MarkerHelper(activity, googleMap);
        linearLayout = activity.findViewById(R.id.nearby_stores_linear);
    }

    public void showNearbyStores(LatLng latLng, double maxDistance) {
        linearLayout.removeAllViews();
        ArrayList<Store> storeArrayList = storeManager.getNearbyStores(latLng.latitude, latLng.longitude, maxDistance);

        for (final Store store : storeArrayList) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            @SuppressLint("InflateParams")
            LinearLayout nearbyStore = (LinearLayout) inflater.inflate(R.layout.partial_nearby_stores, null, false);
            ImageView imageView = (ImageView) nearbyStore.getChildAt(0);

            TextView textView = (TextView) nearbyStore.getChildAt(1);
            textView.setText(store.getName());

            if (store.getImage() == null) {
                imageView.setImageDrawable(markerHelper.getDrawableByType(store.getType().toUpperCase()));
            } else {
                GlideApp.with(activity)
                        .load(store.getImage())
                        .encodeQuality(10)
                        .circleCrop()
                        .into(imageView);
            }

            nearbyStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LatLng latLng1 = new LatLng(store.getLat(), store.getLng());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, (float) 16.29));
                }
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getPixelsFromDp(80), LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            nearbyStore.setLayoutParams(params);

            linearLayout.addView(nearbyStore);
        }
    }

    private int getPixelsFromDp(int dp) {
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }
}
