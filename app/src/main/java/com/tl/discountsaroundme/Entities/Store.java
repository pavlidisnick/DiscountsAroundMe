package com.tl.discountsaroundme.Entities;


import com.google.android.gms.maps.model.LatLng;

public class Store {
    private String name;
    private String type;
    private LatLng latLng;

    public Store(String name, String type, double lat, double lng) {
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

    public LatLng getLatLng() {
        return latLng;
    }
}
