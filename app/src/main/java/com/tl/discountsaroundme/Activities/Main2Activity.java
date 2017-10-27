package com.tl.discountsaroundme.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.Entities.Item;
import com.tl.discountsaroundme.Entities.Store;
import com.tl.discountsaroundme.ItemSpaceDecoration;
import com.tl.discountsaroundme.ItemViewAdapter;
import com.tl.discountsaroundme.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    ArrayList<Item> DiscountItems = new ArrayList<Item>();
    private DatabaseReference mDBDiscountItems;
    ItemViewAdapter adapter;
    //changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2_layout);

        mDBDiscountItems = FirebaseDatabase.getInstance().getReference("/items");

        mRecyclerView = (RecyclerView) findViewById(R.id.item_grid);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        adapter = new ItemViewAdapter(this,DiscountItems);
        mRecyclerView.setAdapter(adapter);
        //ItemDecoration for spacing between items
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(16);
        mRecyclerView.addItemDecoration(decoration);
      GetTopDiscounts();
    }

    public void GetTopDiscounts(){
        mDBDiscountItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                Item item = child.getValue(Item.class);
                DiscountItems.add(item);
                adapter.notifyDataSetChanged();}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
