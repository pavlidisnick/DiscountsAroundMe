package com.tl.discountsaroundme.FirebaseData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchHistory {
    private DatabaseReference historyRef;
    private ArrayList<String> historyList = new ArrayList<>();

    /**
     * Initialize historyList
     */
    public SearchHistory(String userId) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("/user_references");
        historyRef = databaseRef.child("history").child(userId);

        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                historyList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String historyString = child.getValue(String.class);
                    historyList.add(historyString);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<String> getHistoryList() {
        return historyList;
    }

    public void newSearchAdd(String search) {
        if (!historyList.contains(search))
            historyRef.push().setValue(search);
    }
}
