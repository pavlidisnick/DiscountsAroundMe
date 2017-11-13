package com.tl.discountsaroundme.FirebaseData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Druit on 11/11/2017.
 */

public class SearchHistory {
    private DatabaseReference search_history = FirebaseDatabase.getInstance().getReference("/user_references");

    private void NewSearchAdd(String search) {

//in Progress
        search_history.child("search_history").child("history").setValue("test");
    }
}
