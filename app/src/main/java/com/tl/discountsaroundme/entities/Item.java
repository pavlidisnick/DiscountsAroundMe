package com.tl.discountsaroundme.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Item {
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

    private BigDecimal getFinalPrice(double price, double discount) {
        BigDecimal priceBD = new BigDecimal(price);
        BigDecimal discountBD = new BigDecimal(discount);
        return priceBD.subtract(priceBD.multiply(discountBD).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
    }

    public String getFinalPrice(String priceString, String discountString) {
        priceString = priceString.replace("$", "").trim();
        double price = Double.parseDouble(priceString);
        double discount = Double.parseDouble(discountString);
        return "$" + String.valueOf(getFinalPrice(price, discount).setScale(2, RoundingMode.HALF_UP));
    }
}