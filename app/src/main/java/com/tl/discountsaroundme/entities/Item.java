package com.tl.discountsaroundme.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Item implements Serializable {
    private String id;
    private String name;
    private String type;
    private String description;
    private double price;
    private double discount;
    private String picture;
    private String store;

    private boolean isInCart = false;

    public Item() {
    }

    public Item(String id, String name, String type, double price, double discount,
                String description, String picture, String store) {
        this.id = id;
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

    public String getFinalPrice() {
        BigDecimal priceBD = new BigDecimal(price);
        BigDecimal discountBD = new BigDecimal(discount);
        BigDecimal result = priceBD.subtract(priceBD.multiply(discountBD).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        return String.valueOf(result.setScale(2, RoundingMode.HALF_EVEN));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isInCart() {
        return isInCart;
    }

    public void setInCart(boolean inCart) {
        isInCart = inCart;
    }
}