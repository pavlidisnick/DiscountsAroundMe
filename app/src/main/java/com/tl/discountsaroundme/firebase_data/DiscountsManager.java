package com.tl.discountsaroundme.firebase_data;


import com.tl.discountsaroundme.entities.Item;
import com.tl.discountsaroundme.ui_controllers.ItemViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class DiscountsManager {
    private ItemViewAdapter adapter;

    private ArrayList<Item> discountItems = new ArrayList<>();
    private ArrayList<Item> showingList = new ArrayList<>();

    /**
     * @param adapter ItemViewAdapter to notify the listView for data changes
     */
    public void setAdapter(ItemViewAdapter adapter) {
        this.adapter = adapter;
    }

    public ArrayList<Item> getTopDiscounts(int discountThreshold) {
        ArrayList<Item> matchingArray = new ArrayList<>();

        for (Item item : discountItems) {
            if (item.getDiscount() >= discountThreshold)
                matchingArray.add(item);
        }
        return matchingArray;
    }

    /**
     * Invoked on search, based on the searchQuery it changes the list
     *
     * @param searchQuery the search String for discounts
     */
    public ArrayList<Item> getDiscountsByName(String searchQuery) {
        searchQuery = searchQuery.toUpperCase();
        ArrayList<Item> matchingArray = new ArrayList<>();

        for (Item item : discountItems) {
            String name = item.getName().toUpperCase();
            if (name.contains(searchQuery))
                matchingArray.add(item);
        }
        return matchingArray;
    }

    private void fillListAndNotify(List<Item> itemList) {
        discountItems.clear();
        discountItems.addAll(itemList);
        adapter.notifyDataSetChanged();
    }

    /**
     * Empties the discountItems list
     */
    public void clearTopDiscounts() {
        discountItems.clear();
    }

    public ArrayList<Item> getDiscountItems() {
        return discountItems;
    }

    /**
     * @return the names of the discounted items
     */
    public ArrayList<String> getDiscountNames() {
        ArrayList<String> discountNamesList = new ArrayList<>();
        for (Item item : discountItems)
            discountNamesList.add(item.getName());
        return discountNamesList;
    }

    /**
     * Checks the category of every item and changes accordingly
     * Invoked by the categories buttons onClickListener
     *
     * @param category the item category which is also the category button text
     */
    public ArrayList<Item> getDiscountsByCategory(String category) {
        category = category.toUpperCase();
        ArrayList<Item> categoryItems = new ArrayList<>();

        for (Item item : showingList) {
            String type = item.getType().toUpperCase();
            if (type.contains(category))
                categoryItems.add(item);
        }
        return categoryItems;
    }

    /**
     * Checks if all the items and returns a list of items based on the store they belong
     * Also it checks categoryItems
     *
     * @param store the store you want to receive it's items
     * @return an array of items belonging on the store
     */
    public ArrayList<Item> getTopDiscountsByStore(String store, int discountThreshold) {
        store = store.toUpperCase().trim();
        ArrayList<Item> storeItems = new ArrayList<>();

        for (Item item : discountItems) {
            String storeName = item.getStore().toUpperCase().trim();
            if (store.equals(storeName) && item.getDiscount() >= discountThreshold)
                storeItems.add(item);
        }
        return storeItems;
    }

    public Item getTopItemByStore(String store) {
        double max = 0;
        Item topItem = null;

        for (Item item : discountItems) {
            if (store.equals(item.getStore()) && item.getDiscount() >= max) {
                max = item.getDiscount();
                topItem = item;
            }
        }
        return topItem;
    }
}
