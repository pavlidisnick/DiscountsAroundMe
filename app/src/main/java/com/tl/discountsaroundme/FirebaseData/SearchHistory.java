package com.tl.discountsaroundme.FirebaseData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.Entities.History;

import java.util.ArrayList;

/**
 * Created by Druit on 11/11/2017.
 */

public class SearchHistory {
    private DatabaseReference mDatasearch_history = FirebaseDatabase.getInstance().getReference("/user_references");
    private ArrayList<String> Searches = new ArrayList<String>();
    private void NewSearchAdd(String search) {



        mDatasearch_history.child("search_history").setValue("test");
        mDatasearch_history.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    History Historyname = child.getValue(History.class);
                    //To Be Continue
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
