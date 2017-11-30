package com.tl.discountsaroundme.discounts;

import com.tl.discountsaroundme.entities.Item;

import java.util.ArrayList;

public class DiscountValidator {

    /**
     * Gets a list of items and returns only the top discounts based on the discountThreshold
     *
     * @param itemList          List of all items
     * @param discountThreshold a required integer to filter the best discounts
     * @return top discounts
     */
    public ArrayList<Item> getTopDiscounts(ArrayList<Item> itemList, int discountThreshold) {
        for (Item item : itemList) {
            if (item.getDiscount() < discountThreshold)
                itemList.remove(item);
        }
        return itemList;
    }

    /**
     * Get a queried list of items that match the searchQuery string and the item names
     *
     * @param itemList list of items you want to query through
     * @param searchQuery a search String for the items we want to keep
     * @return a matching list of items
     */
    public ArrayList<Item> getDiscountsByName(ArrayList<Item> itemList, String searchQuery) {
        searchQuery = searchQuery.toUpperCase();

        for (Item item : itemList) {
            String itemName = item.getName().toUpperCase();
            if (!itemName.contains(searchQuery))
                itemList.remove(item);
        }
        return itemList;
    }
}
