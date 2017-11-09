package com.tl.discountsaroundme;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.FirebaseData.DiscountsManager;
import com.tl.discountsaroundme.UiControllers.ItemViewAdapter;

public class CategoryListener {
    public CategoryListener(final AddCategoryToLayout addCategoryToLayout, final DiscountsManager discountsManager) {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("/categories");

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addCategoryToLayout.clearCategories();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    String category = itemSnapshot.child("name").getValue(String.class);
                    addCategoryToLayout.addCategory(category, discountsManager);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
