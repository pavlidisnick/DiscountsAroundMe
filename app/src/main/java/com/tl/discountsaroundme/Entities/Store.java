package com.tl.discountsaroundme.Entities;

import com.google.android.gms.maps.model.LatLng;

public class Store {
    private String name;
    private String type;
    private double lat;
    private double lng;

    private Store() {}

    public Store(String name, String type, double lat, double lng) {
        this.name = name;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return this.name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
