package com.tl.discountsaroundme.discounts;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Retrieves all categories from the database and then adds the category buttons
 */
public class FetchCategories {
    public FetchCategories(final AddCategoryToLayout addCategoryToLayout) {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("/categories");

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addCategoryToLayout.clearCategories();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    String category = itemSnapshot.child("name").getValue(String.class);
                    addCategoryToLayout.addCategory(category);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
