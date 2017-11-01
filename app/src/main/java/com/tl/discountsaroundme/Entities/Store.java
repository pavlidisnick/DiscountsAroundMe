package com.tl.discountsaroundme.Entities;

public class Store {


    private String name;
    private String type;
    private double lat;
    private double lng;

    private String image;
    private String description;



    private String ownerUID;

    public Store() {
    }

    public Store(String name, String type, double lat, double lng, String image, String description , String OwnerUID) {
        this.description = description;
        this.image = image;
        this.name = name;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
        this.ownerUID = OwnerUID;
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

    public String Description() {return description;}

    public String getOwnerUID(){return ownerUID;}

    public void setOwnerUID(String ownerUID) {this.ownerUID = ownerUID;}

    public void setName(String name) {this.name = name;}

    public void setType(String type) {this.type = type;}

    public void setLat(double lat) {this.lat = lat;}

    public void setLng(double lng) {this.lng = lng;}

    public void setImage(String image) {this.image = image;}

    public void setDescription(String description) {this.description = description;}


}
