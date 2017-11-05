package com.tl.discountsaroundme.Entities;

public class Store {
    private String name;
    private String type;
    private double lat;
    private double lng;

    private String image;
    private String description;


    private Store() {
    }

    public Store(String name, String type, double lat, double lng, String image, String description) {
        this.description = description;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public String Description() {
        return description;
    }
}
