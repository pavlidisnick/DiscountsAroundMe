package com.tl.discountsaroundme.firebase_data;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.entities.Item;

import java.util.ArrayList;

public class FetchDiscounts {
    private ArrayList<Item> allItems = new ArrayList<>();

    /**
     * @param firebaseDatabase FirebaseDatabase instance
     */
    public FetchDiscounts(FirebaseDatabase firebaseDatabase) {
        DatabaseReference mDBDiscountItems = firebaseDatabase.getReference("/items");

        mDBDiscountItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allItems.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Item item = child.getValue(Item.class);
                    if (item != null)
                        allItems.add(item);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public ArrayList<Item> getAllItems() {
        return allItems;
    }
}
