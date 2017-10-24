package com.tl.discountsaroundme.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.R;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {

    Button btMap;
    Button btSearch;
    EditText etItemSearch;
    ArrayList<String> listDiscountItems = new ArrayList<>();
    ArrayList<String> listSearchItems = new ArrayList<>();
    ArrayAdapter<String> adapter,searchAdapter;
    private DatabaseReference mDbRefDiscounts ;
    private DatabaseReference mDbRefSearch ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbRefDiscounts = FirebaseDatabase.getInstance().getReference("/shops/1/items");

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
        //Create the list adapters  and set it on the list views
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listDiscountItems);
        lvDiscountsList.setAdapter(adapter);
        searchAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listSearchItems);
        lvSearchList.setAdapter(searchAdapter);

        //Todays Top Discounts
        mDbRefDiscounts.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String price = dataSnapshot.child("price").getValue(String.class);
                    String discount = dataSnapshot.child("discount").getValue(String.class);
                listDiscountItems.add(name + "   "+ price + "  " + discount);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.equals(btMap)) {
            Intent MapActivity = new Intent(this, MapActivity.class);
            startActivity(MapActivity);
        }
        else if(v.equals(btSearch)) {
            final String userSearch = etItemSearch.getText().toString().toLowerCase();
            mDbRefSearch = FirebaseDatabase.getInstance().getReference();
           // DatabaseReference mRef = mDbRefSearch.child("shops/1/items");
            Query searchQuery = mDbRefSearch.child("items").orderByKey().equalTo(userSearch);
            searchQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){
                        // do something

                        listSearchItems.add(dataSnapshot.child(userSearch).getKey().toString()+ "   "
                                +"price: "+dataSnapshot.child(userSearch+"/price").getValue().toString() + "   "
                                +"discount: "+dataSnapshot.child(userSearch).child("discount").getValue().toString()+"% "
                                +"from: "+dataSnapshot.child(userSearch).child("shop").getValue().toString());
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



            //Search Code.
        }
    }

}
