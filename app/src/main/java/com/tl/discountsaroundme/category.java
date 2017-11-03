package com.tl.discountsaroundme;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class category {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private ArrayList<String> itemsCat = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();


    public void findNewCategories(){
        getCategories();
        getCategoriesFromItems();

        for (String e: itemsCat){
            if(!categories.contains(e)){
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
                    if (!categories.contains(itemName)) {
                        categories.add(itemName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return categories;
    }

    public ArrayList<String> getCategoriesFromItems(){


        database.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    String itemName = itemSnapshot.child("name").getValue(String.class);
                    if (!itemsCat.contains(itemName)) {
                        itemsCat.add(itemName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return itemsCat;
    }

    public ArrayList<String> getCategorie() {
        return categories;
    }

    public ArrayList<String> getItems() {
        return itemsCat;
    }
}
