package com.tl.discountsaroundme.entities;

import java.io.Serializable;

public class Item implements Serializable{
    private String name;
    private String type;
    private String description;
    private double price;
    private double discount;
    private String picture;
    private String store;

    public Item() {
    }

    public Item(String name, String type, double price, double discount, String description, String picture, String store) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.discount = discount;
        this.description = description;
        this.picture = picture;
        this.store = store;
    }


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public double getDiscount() {
        return discount;
    }

    public String getPicture() {
        return picture;
    }

    public String getStore() {
        return store;
    }
}
