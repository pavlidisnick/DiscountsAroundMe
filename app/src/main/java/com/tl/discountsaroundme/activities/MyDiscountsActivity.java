package com.tl.discountsaroundme.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

    ArrayList<Item> shopDiscounts = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_discounts);

        shopDiscounts = (ArrayList<Item>) getIntent().getSerializableExtra("SHOP_DISCOUNTS");

        DiscountsAdapter adapter = new DiscountsAdapter(this, shopDiscounts);
        ListView listView = (ListView) findViewById(R.id.list_discounts);

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

                Intent itemDetailsActivity = new Intent(getApplicationContext(), StoreItemDetailsActivity.class);

                itemDetailsActivity.putExtra(DATA_ITEM_DETAILS, Description);
                itemDetailsActivity.putExtra(DATA_ITEM_NAME, Name);
                itemDetailsActivity.putExtra(DATA_ITEM_STORE, Store);
                itemDetailsActivity.putExtra(DATA_ITEM_PRICE, Price);
                itemDetailsActivity.putExtra(DATA_IMAGE, Picture);
                itemDetailsActivity.putExtra(DATA_TYPE, Type);
                itemDetailsActivity.putExtra(DATA_DISCOUNT, Discount);
                startActivity(itemDetailsActivity);
            }
        });

    }

}
