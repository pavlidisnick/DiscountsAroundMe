package com.tl.discountsaroundme;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Marker {


    public Marker(final GoogleMap m) {
        DatabaseReference databaseStores = FirebaseDatabase.getInstance().getReference("Stores");

        databaseStores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot storesSnapshot: dataSnapshot.getChildren()){
                    try {
                        Store store = storesSnapshot.getValue(Store.class);
                        m.addMarker(new MarkerOptions().position(store.getLatLng()).title(store.getName()));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
