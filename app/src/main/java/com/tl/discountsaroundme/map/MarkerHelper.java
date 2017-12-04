package com.tl.discountsaroundme.map;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.entities.Store;

import java.util.ArrayList;


public class MarkerHelper {
    private Fragment fragment;
    private GoogleMap googleMap;

    public MarkerHelper(Fragment fragment, GoogleMap googleMap) {
        this.fragment = fragment;
        this.googleMap = googleMap;
    }

    public Drawable getDrawableByType(String code) {
        return fragment.getResources().getDrawable(getDrawableId(code));
    }

    public int getDrawableId(String code) {
        int i = -1;
        for (String cc : fragment.getResources().getStringArray(R.array.codes)) {
            i++;
            if (cc.equals(code))
                break;
        }
        try {
            String resource = fragment.getResources().getStringArray(R.array.names)[i];
            return fragment.getResources().getIdentifier(resource, "drawable", fragment.getActivity().getPackageName());
        } catch (Exception e) {
            return R.drawable.marker_shop;
        }
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = getBitmapByDrawable(drawable);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public Bitmap getBitmapByDrawable(Drawable drawable) {
        return Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    }

    void addStoreMarker(Store store) {
        Drawable circleDrawable = getDrawableByType(store.getType().toUpperCase());
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);

        MarkerOptions marker = new MarkerOptions()
                .position(new LatLng(store.getLat(), store.getLng()))
                .title(store.getName())
                .snippet(store.getType())
                .flat(true)
                .icon(markerIcon);
        googleMap.addMarker(marker);
    }

    public void addMarkersFromList(ArrayList<Store> stores) {
        googleMap.clear();
        for (Store store : stores) {
            addStoreMarker(store);
        }
    }

    void animateCamera(Store store) {
        LatLng latLng = new LatLng(store.getLat(), store.getLng());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 16.29));
    }
}
