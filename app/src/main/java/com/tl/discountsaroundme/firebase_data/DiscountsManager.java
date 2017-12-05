package com.tl.discountsaroundme.firebase_data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.entities.Item;
import com.tl.discountsaroundme.ui_controllers.ItemViewAdapter;

import java.util.ArrayList;

public class DiscountsManager {
    private ItemViewAdapter adapter;
    private ArrayList<Item> allItems = new ArrayList<>();
    private ArrayList<Item> showingItems = new ArrayList<>();

    public void setAdapter(ItemViewAdapter adapter) {
        this.adapter = adapter;
    }

    public void getTopDiscounts(int discountThreshold) {
        showingItems.clear();

        for (Item item : allItems) {
            if (item.getDiscount() >= discountThreshold)
                showingItems.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Invoked on search, based on the searchQuery it changes the list
     *
     * @param searchQuery the search String for discounts
     */
    public void getDiscountsByName(String searchQuery) {
        searchQuery = searchQuery.toUpperCase();
        showingItems.clear();

        for (Item item : allItems) {
            String name = item.getName().toUpperCase();
            if (name.contains(searchQuery))
                showingItems.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * @return the names of the discounted items
     */
    public ArrayList<String> getDiscountNames() {
        ArrayList<String> discountNamesList = new ArrayList<>();
        for (Item item : allItems)
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
        category = category.toUpperCase();
        showingItems.clear();

        for (Item item : allItems) {
            String type = item.getType().toUpperCase();
            if (type.contains(category))
                showingItems.add(item);
        }
        adapter.notifyDataSetChanged();
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

        for (Item item : allItems) {
            String storeName = item.getStore().toUpperCase().trim();
            if (store.equals(storeName) && item.getDiscount() >= discountThreshold)
                storeItems.add(item);
        }
        return storeItems;
    }

    public Item getTopItemByStore(String store) {
        double max = 0;
        Item topItem = null;

        for (Item item : allItems) {
            if (store.equals(item.getStore()) && item.getDiscount() >= max) {
                max = item.getDiscount();
                topItem = item;
            }
        }
        return topItem;
    }

    /**
     * @param firebaseDatabase FirebaseDatabase instance
     */
    public void showTopDiscounts(FirebaseDatabase firebaseDatabase, final int discountThreshold) {
        DatabaseReference mDBDiscountItems = firebaseDatabase.getReference("/items");

        mDBDiscountItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showingItems.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Item item = child.getValue(Item.class);
                    if (item != null && item.getDiscount() >= discountThreshold)
                        showingItems.add(item);
                }
                allItems.clear();
                allItems.addAll(showingItems);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    void showTopDiscounts(ArrayList<Item> itemList) {
        allItems = itemList;
    }

    public ArrayList<Item> getShowingItems() {
        return showingItems;
    }
}
