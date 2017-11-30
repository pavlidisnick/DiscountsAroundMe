package com.tl.discountsaroundme.firebase_data;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.entities.Item;
import com.tl.discountsaroundme.fragments.DiscountsTab;
import com.tl.discountsaroundme.ui_controllers.ItemViewAdapter;

import java.util.ArrayList;

public class DiscountsManager {
    private DatabaseReference mDBDiscountItems;
    private ItemViewAdapter adapter;

    private ArrayList<Item> discountItems = new ArrayList<>();
    private ArrayList<Item> unchangedList = new ArrayList<>();

    /**
     * @param firebaseDatabase FirebaseDatabase instance
     */
    public DiscountsManager(FirebaseDatabase firebaseDatabase) {
        mDBDiscountItems = firebaseDatabase.getReference("/items");
    }

    /**
     * @param adapter ItemViewAdapter to notify the listView for data changes
     */
    public void setAdapter(ItemViewAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Fetches the items from the db and it checks if the discountValue is eq or qt than the user prefs
     * Based on the results the discountItems list is filled
     * Based on the results the unchangedList is filled
     * The adapter is notified for data changes
     */
    public void getTopDiscounts() {
        mDBDiscountItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                discountItems.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Item item = child.getValue(Item.class);
                    if (item != null && item.getDiscount() >= DiscountsTab.discountValue)
                        discountItems.add(item);
                }
                adapter.notifyDataSetChanged();
                unchangedList.clear();
                unchangedList.addAll(discountItems);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Sets the discountedItems list and the unchangedList based on the db it received
     * But it doesn't notify the adapter for data changes
     */
    public void getDiscounts() {
        mDBDiscountItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                discountItems.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Item item = child.getValue(Item.class);
                    if (item != null && item.getDiscount() >= DiscountsTab.discountValue)
                        discountItems.add(item);
                }

                unchangedList.clear();
                unchangedList.addAll(discountItems);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Invoked on search, based on the searchQuery it changes the list
     *
     * @param searchQuery the search String for discounts
     */
    public void getDiscountsByName(String searchQuery) {
        searchQuery = searchQuery.toUpperCase();
        ArrayList<Item> matchingArray = new ArrayList<>();
        for (Item item : unchangedList) {
            String name = item.getName().toUpperCase();
            if (name.contains(searchQuery))
                matchingArray.add(item);
        }
        discountItems.clear();
        discountItems.addAll(matchingArray);
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
        for (Item item : unchangedList)
            discountNamesList.add(item.getName());
        return discountNamesList;
    }

    /**
     * Checks the category of every item and changes accordingly
     * Invoked by the categories buttons onClickListener
     *
     * @param category the item category which is also the category button text
     */
    public void getDiscountsByCategory(String category) {
        discountItems.clear();
        category = category.toUpperCase();
        for (Item item : unchangedList) {
            String type = item.getType().toUpperCase();
            if (type.contains(category))
                discountItems.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Checks if all the items and returns a list of items based on the store they belong
     * Also it checks if
     *
     * @param store the store you want to receive it's items
     * @return an array of items belonging on the store
     */
    public ArrayList<Item> getTopDiscountsByStore(String store) {
        store = store.toUpperCase().trim();
        ArrayList<Item> storeItems = new ArrayList<>();
        for (Item item : unchangedList) {
            String storeName = item.getStore().toUpperCase().trim();
            if (store.equals(storeName) && item.getDiscount() >= DiscountsTab.discountValue)
                storeItems.add(item);
        }
        return storeItems;
    }

    public Item getTopItemByStore(String store) {
        double max = 0;
        Item topItem = null;
        for (Item item : unchangedList) {
            if (store.equals(item.getStore()) && item.getDiscount() >= max) {
                max = item.getDiscount();
                topItem = item;
            }
        }
        return topItem;
    }
}
