package com.tl.discountsaroundme.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.tl.discountsaroundme.ui_controllers.ManageMyDiscountsAdapter;
import com.tl.discountsaroundme.ui_controllers.StatusBar;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;
// TODO: add a waiting period when removing items so the recycler view can be notified for the changes
public class MyDiscountsActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, OnMenuItemClickListener {

    private ArrayList<Item> shopDiscounts = new ArrayList<>();
    private ArrayList<Item> selectedItems = new ArrayList<>();
    private ArrayList<View> selectedViews = new ArrayList<>();

    private ProgressDialog pd;
    private ManageMyDiscountsAdapter adapter;
    private ListView listView;

    private ContextMenuDialogFragment contextMenuDialogFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_discounts);
        new StatusBar(this);

        listView = findViewById(R.id.list_discounts);
        adapter = new ManageMyDiscountsAdapter(this, shopDiscounts);
        listView.setAdapter(adapter);

        ImageView moreOptions = findViewById(R.id.manage_more_options);
        moreOptions.setOnClickListener(this);

        contextMenu();

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
                    listView.setOnItemClickListener(MyDiscountsActivity.this);
                    listView.setOnItemLongClickListener(MyDiscountsActivity.this);
                }
                adapter.notifyDataSetChanged();
                pd.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void contextMenu() {
        MenuObject empty = new MenuObject();
        Drawable emptyDrawable = getResources().getDrawable(R.drawable.ic_more_vert_black_24dp);
        int color = Color.parseColor("#FFFFFF");
        emptyDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        empty.setDrawable(emptyDrawable);
        empty.setBgColor(getResources().getColor(R.color.black_with_blue_accent));


        MenuObject delete = new MenuObject("Delete");
        delete.setResource(R.drawable.ic_close);
        delete.setBgColor(getResources().getColor(R.color.black_with_blue_accent));

        MenuObject edit = new MenuObject("Edit");
        edit.setResource(R.drawable.ic_mode_edit);
        edit.setBgColor(getResources().getColor(R.color.black_with_blue_accent));

        List<MenuObject> menuObjects = new ArrayList<>();
        menuObjects.add(empty);
        menuObjects.add(delete);
        menuObjects.add(edit);

        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize(120);
        menuParams.setMenuObjects(menuObjects);
        menuParams.setClosableOutside(true);
        // set other settings to meet your needs
        contextMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        contextMenuDialogFragment.setItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        contextMenuDialogFragment.show(getSupportFragmentManager(), "ContextMenuDialogFragment");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item = shopDiscounts.get(position);

        if (selectedItems.isEmpty()) {
            Intent itemDetailsActivity = new Intent(getApplicationContext(), StoreItemDetailsActivity.class);
            itemDetailsActivity.putExtra("ITEM", shopDiscounts.get(position));

            startActivity(itemDetailsActivity);
        } else if (selectedItems.contains(item)) {
            setUnselectedItem(view, position);
        } else {
            setSelectedItem(view, position);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        setSelectedItem(view, position);
        return true;
    }

    private void setSelectedItem(View view, int position) {
        view.setBackground(getResources().getDrawable(R.color.orange));
        selectedItems.add(shopDiscounts.get(position));
        selectedViews.add(view);
    }

    private void setUnselectedItem(View view, int position) {
        view.setBackgroundResource(0);
        selectedItems.remove(shopDiscounts.get(position));
        selectedViews.remove(view);
    }

    @Override
    public void onBackPressed() {
        if (selectedItems.isEmpty())
            super.onBackPressed();
        else {
            for (View view : selectedViews) {
                view.setBackgroundResource(0);
            }
            selectedViews.clear();
            selectedItems.clear();
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        if (position == 1) {
            for (Item item : selectedItems) {
                deleteDiscount(item);
            }
        }
    }

    private void deleteDiscount(Item item) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("items");
        ref.child(item.getId()).removeValue();
    }
}
