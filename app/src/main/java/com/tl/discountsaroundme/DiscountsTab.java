package com.tl.discountsaroundme;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.Entities.Item;

import java.util.ArrayList;

/**
 * Created by rezu on 27/10/2017.
 */

public class DiscountsTab extends Fragment{

    RecyclerView mRecyclerView;
    ArrayList<Item> DiscountItems = new ArrayList<Item>();
    private DatabaseReference mDBDiscountItems;
    ItemViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grid_layout, container, false);


        mDBDiscountItems = FirebaseDatabase.getInstance().getReference("/items");

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.item_grid);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        adapter = new ItemViewAdapter(getActivity(),DiscountItems);
        mRecyclerView.setAdapter(adapter);
        //ItemDecoration for spacing between items
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(16);
        mRecyclerView.addItemDecoration(decoration);
        GetTopDiscounts();
        return rootView;
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
