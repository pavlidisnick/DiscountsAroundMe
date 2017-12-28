package com.tl.discountsaroundme.entities;

public class ItemValidator {

    public boolean areStringsEmpty(String name, String description, String category, String price, String discount) {
        return name.isEmpty() || description.isEmpty() || category.isEmpty()
                || price.isEmpty() || discount.isEmpty();
    }

    public boolean isDiscountInRange(double discount) {
        return discount <= 100 && discount >= 0;
    }
}
