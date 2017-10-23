package com.tl.discountsaroundme;

/**
 * Created by rezu on 23/10/2017.
 */

public class ItemsClass {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float price;

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float discount;

    public ItemsClass(String name, float price,float discount){
        this.name= name;
        this.price=price;
        this.discount= discount;

    }
}
