package com.tl.discountsaroundme;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.entities.Item;
import com.tl.discountsaroundme.ui_controllers.ItemViewAdapter;

import java.util.ArrayList;

public class ShoppingCart {
    private ItemViewAdapter adapter;
    private ArrayList<Item> cartItems = new ArrayList<>();
    private DatabaseReference shoppingCartRef;

    public ShoppingCart(FirebaseDatabase firebaseDatabase, String userId) {
        String dbPath = "/user_references/shopping_cart/" + userId;
        shoppingCartRef = firebaseDatabase.getReference(dbPath);
    }

    public void setAdapter(ItemViewAdapter adapter) {
        this.adapter = adapter;
    }

    public ArrayList<Item> getCartItems() {
        return cartItems;
    }

    public void retrieveCartItems() {
        shoppingCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartItems.clear();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Item item = child.getValue(Item.class);
                    if (item != null) {
                        item.setInCart(true);
                        cartItems.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void addToCart(Item item) {
        item.setInCart(true);
        shoppingCartRef.child(item.getId()).setValue(item);
    }

    public void removeFromCart(Item item) {
        item.setInCart(false);
        cartItems.remove(item);
        shoppingCartRef.child(item.getId()).removeValue();
    }
}
