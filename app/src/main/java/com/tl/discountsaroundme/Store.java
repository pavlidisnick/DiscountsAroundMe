package com.tl.discountsaroundme;


import com.google.android.gms.maps.model.LatLng;

public class Store {
    private String id;
    private String name;
    private LatLng latLng;
    private String type;

    Store(String name, String type, double lat, double lng) {
        this.name = name;
        this.type = type;
        latLng = new LatLng(lat, lng);
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return this.name;
    }

    LatLng getLatLng() {
        return latLng;
    }
}
