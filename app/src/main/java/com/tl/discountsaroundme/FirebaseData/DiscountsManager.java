package com.tl.discountsaroundme.FirebaseData;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.Entities.Item;
import com.tl.discountsaroundme.Entities.Store;
import com.tl.discountsaroundme.Fragments.DiscountsTab;
import com.tl.discountsaroundme.UiControllers.ItemViewAdapter;

import java.util.ArrayList;

public class DiscountsManager {
    private DatabaseReference mDBDiscountItems = FirebaseDatabase.getInstance().getReference("/items");
    private ArrayList<Item> discountItems = new ArrayList<>();
    private ArrayList<Item> unchangedList = new ArrayList<>();
    private ItemViewAdapter adapter;
    private StoreManager storeManager = new StoreManager();

    public void setAdapter(ItemViewAdapter adapter) {
        this.adapter = adapter;
    }

    public void getTopDiscounts() {
        mDBDiscountItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                discountItems.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Item item = child.getValue(Item.class);
                    if (item.getDiscount() >= DiscountsTab.discountValue)
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

    public void clearTopDiscounts() {
        discountItems.clear();
    }

    public ArrayList<Item> getDiscountItems() {
        return discountItems;
    }

    public ArrayList<String> getSuggestionsDiscounts() {
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

    public void getDiscountsByStore (String store){
        discountItems.clear();

        for (Item item : unchangedList) {
            String type = item.getStore().toUpperCase();
            if (type.contains())
                discountItems.add(item);
        }
        adapter.notifyDataSetChanged();
    }
}
