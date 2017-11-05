package com.tl.discountsaroundme.FirebaseData;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.Entities.Item;
import com.tl.discountsaroundme.Fragments.DiscountsTab;
import com.tl.discountsaroundme.UiControllers.ItemViewAdapter;

import java.util.ArrayList;

public class DiscountsManager {
    private DatabaseReference mDBDiscountItems = FirebaseDatabase.getInstance().getReference("/items");
    private ArrayList<Item> discountItems = new ArrayList<>();

    public void getTopDiscounts(final ItemViewAdapter adapter) {
        mDBDiscountItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Item item = child.getValue(Item.class);
                    if (item.getDiscount() >= DiscountsTab.discountValue) {
                        discountItems.add(item);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void clearTopDiscounts() {
        discountItems.clear();
    }

    public ArrayList<Item> getDiscountItems() {
        return discountItems;
    }
}
