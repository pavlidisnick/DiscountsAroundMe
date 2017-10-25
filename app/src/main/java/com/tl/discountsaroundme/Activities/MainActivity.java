package com.tl.discountsaroundme.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.Entities.Item;
import com.tl.discountsaroundme.ItemArrayAdapter;
import com.tl.discountsaroundme.R;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {

    Button btMap;
    Button btSearch;
    EditText etItemSearch;
    ArrayList<Item> listDiscountItems = new ArrayList<Item>();
    ArrayList<Item> listSearchItems = new ArrayList<Item>();
    private DatabaseReference mDbRefDiscounts ;
    private DatabaseReference mDbRefSearch ;
    ItemArrayAdapter discountAdapter, searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbRefDiscounts = FirebaseDatabase.getInstance().getReference("/id");

        btMap = findViewById(R.id.btMap);
        btSearch = findViewById(R.id.btSearch);
        TextView tvWelcome =  findViewById(R.id.tvWelcomeMessage);
        TextView tvTopDiscounts =  findViewById(R.id.tvTopDiscounts);
        etItemSearch =  findViewById(R.id.etItemSearch);
        ListView lvDiscountsList = (ListView) findViewById(R.id.lvDiscounts);
        ListView lvSearchList =  (ListView) findViewById(R.id.lvItemsSearched);
        btMap.setOnClickListener(this);
        btSearch.setOnClickListener(this);

        //Get the username from the login activity And set it on the welcome Page
        Bundle extras = getIntent().getExtras();
            if (extras != null){
                String value = extras.getString("Username");
                tvWelcome.setText("Welcome "+ value);
            }
        discountAdapter = new ItemArrayAdapter(listDiscountItems,this,R.layout.activity_main);
        searchAdapter = new ItemArrayAdapter(listSearchItems,this,R.layout.activity_main);
        lvDiscountsList.setAdapter(discountAdapter);
        lvSearchList.setAdapter(searchAdapter);
        GetTopDiscounts();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btMap)) {
            Intent MapActivity = new Intent(this, MapActivity.class);
            startActivity(MapActivity);
        }
        else if(v.equals(btSearch)) {
            final String userSearch = etItemSearch.getText().toString();//toLowerCase();
            //Search Code.
            GetSearchResults(userSearch);
        }
    }

    public void GetTopDiscounts(){
        mDbRefDiscounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Item item = dataSnapshot.getValue(Item.class);
                listDiscountItems.add(item);
                discountAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void GetSearchResults(final String userSearch){
        mDbRefSearch = FirebaseDatabase.getInstance().getReference();
        Query searchQuery = mDbRefSearch.orderByChild("name").equalTo(userSearch);
        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Item item = child.getValue(Item.class);
                        listSearchItems.add(item);
                    }
                    searchAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "We found something", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"We didnt find anything.",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}

