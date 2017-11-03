package com.tl.discountsaroundme;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class category {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private ArrayList<String> items = new ArrayList<>();
    private ArrayList<String> cat = new ArrayList<>();


    public void findNewCategories(){
        getCategories();
        getCategoriesFromItems();

        for (String e: items){
            if(!cat.contains(e)){
                // query to add the category to firebase
                String id = database.push().getKey();
                database.child("categories").child(id).child("name").setValue(e.toString());
                System.out.println("Success inserted to database");//debugging
            }
        }
    }


    public ArrayList<String> getCategories(){
        database.child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    String itemName = itemSnapshot.child("name").getValue(String.class);
                    if (!cat.contains(itemName)) {
                        cat.add(itemName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return cat;
    }

    public ArrayList<String> getCategoriesFromItems(){


        database.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    String itemName = itemSnapshot.child("name").getValue(String.class);
                    if (!items.contains(itemName)) {
                        items.add(itemName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return items;
    }

    public ArrayList<String> getCategorie() {
        return cat;
    }

    public ArrayList<String> getItems() {
        return items;
    }
}
