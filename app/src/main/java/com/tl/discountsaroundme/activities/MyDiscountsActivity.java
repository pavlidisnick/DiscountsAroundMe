package com.tl.discountsaroundme.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

import static com.tl.discountsaroundme.ui_controllers.ItemViewAdapter.DATA_DISCOUNT;
import static com.tl.discountsaroundme.ui_controllers.ItemViewAdapter.DATA_IMAGE;
import static com.tl.discountsaroundme.ui_controllers.ItemViewAdapter.DATA_ITEM_DETAILS;
import static com.tl.discountsaroundme.ui_controllers.ItemViewAdapter.DATA_ITEM_NAME;
import static com.tl.discountsaroundme.ui_controllers.ItemViewAdapter.DATA_ITEM_PRICE;
import static com.tl.discountsaroundme.ui_controllers.ItemViewAdapter.DATA_ITEM_STORE;
import static com.tl.discountsaroundme.ui_controllers.ItemViewAdapter.DATA_TYPE;

public class MyDiscountsActivity extends AppCompatActivity{

    private ArrayList<Item> shopDiscounts = new ArrayList<>();
    private String ShopName;
    private ProgressDialog pd;
    private DiscountsAdapter adapter;
    private ListView listView;
    private ArrayList<String> keys=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_discounts);

        pd = ProgressDialog.show(this, "", "Just a moment");

        getShopName();
    }

    private void getShopName(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("/shops");
        if (user != null) {
            final String uid = user.getUid();

            categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        String ID = itemSnapshot.child("ownerUID").getValue(String.class);
                        if(ID.matches(uid)){
                            ShopName = itemSnapshot.child("name").getValue(String.class);
                            getStoreDiscounts();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void getStoreDiscounts(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/items");
        Query query = ref.orderByChild("store").equalTo(ShopName);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shopDiscounts.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Item item = data.getValue(Item.class);
                    shopDiscounts.add(item);
                    keys.add(data.getKey());
                }
                setDiscountsToListview();
                pd.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setDiscountsToListview(){

        adapter = new DiscountsAdapter(this, shopDiscounts);
        listView = (ListView) findViewById(R.id.list_discounts);

        listView.clearChoices();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String Name = shopDiscounts.get(i).getName();
                String Description = shopDiscounts.get(i).getDescription();
                String Picture = shopDiscounts.get(i).getPicture();
                String Store = shopDiscounts.get(i).getStore();
                String Type = shopDiscounts.get(i).getType();
                Double Price2 = shopDiscounts.get(i).getPrice();
                Double Discount2 = shopDiscounts.get(i).getDiscount();

                String Price = String.valueOf(Price2);
                String Discount = String.valueOf(Discount2);

                String k = keys.get(i);

                Intent itemDetailsActivity = new Intent(getApplicationContext(), StoreItemDetailsActivity.class);

                itemDetailsActivity.putExtra(DATA_ITEM_DETAILS, Description);
                itemDetailsActivity.putExtra(DATA_ITEM_NAME, Name);
                itemDetailsActivity.putExtra(DATA_ITEM_STORE, Store);
                itemDetailsActivity.putExtra(DATA_ITEM_PRICE, Price);
                itemDetailsActivity.putExtra(DATA_IMAGE, Picture);
                itemDetailsActivity.putExtra(DATA_TYPE, Type);
                itemDetailsActivity.putExtra(DATA_DISCOUNT, Discount);

                itemDetailsActivity.putExtra("KEY",k);

                startActivity(itemDetailsActivity);
            }
        });
    }

}
