package com.tl.discountsaroundme;


import com.google.android.gms.maps.model.LatLng;

public class Store {
    private String id;
    private String name;
    private LatLng latLng;

    public Store(String id, String name, double lat, double lng) {
        this.id = id;
        this.name = name;
        latLng = new LatLng(lat, lng);
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    LatLng getLatLng() {
        return latLng;
    }
}
