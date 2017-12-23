package com.tl.discountsaroundme.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.entities.Item;
import com.tl.discountsaroundme.ui_controllers.DiscountsAdapter;

import java.util.ArrayList;

public class MyDiscountsActivity extends AppCompatActivity {

    private ArrayList<Item> shopDiscounts = new ArrayList<>();
    private ProgressDialog pd;
    private DiscountsAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_discounts);

        listView = findViewById(R.id.list_discounts);
        adapter = new DiscountsAdapter(this, shopDiscounts);
        listView.setAdapter(adapter);

        pd = ProgressDialog.show(this, "", "Just a moment");

        getShopName();
    }

    private void getShopName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("/shops");
        if (user != null) {
            final String uid = user.getUid();

            categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        String id = itemSnapshot.child("ownerUID").getValue(String.class);

                        if (id != null && id.matches(uid)) {
                            String shopName = itemSnapshot.child("name").getValue(String.class);
                            getStoreDiscounts(shopName);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void getStoreDiscounts(String shopName) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/items");
        Query query = ref.orderByChild("store").equalTo(shopName);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shopDiscounts.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Item item = data.getValue(Item.class);
                    shopDiscounts.add(item);
                    setDiscountsToListView(item);
                }
                adapter.notifyDataSetChanged();
                pd.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setDiscountsToListView(final Item item) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent itemDetailsActivity = new Intent(getApplicationContext(), StoreItemDetailsActivity.class);
                itemDetailsActivity.putExtra("ITEM", item);

                startActivity(itemDetailsActivity);
            }
        });
    }
}
