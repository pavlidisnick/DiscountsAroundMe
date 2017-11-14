package com.tl.discountsaroundme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDetailsActivity extends Activity {

    String dataprice,dataitemName,dataitemDetails,datastoreName;
    String imageView;
    TextView Price;
    TextView ItemName;
    TextView ItemDetails;
    TextView StoreName;
    ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Intent intent= getIntent();
        dataprice = intent.getStringExtra("Price");
        dataitemName = intent.getStringExtra("ItemName");
        datastoreName = intent.getStringExtra("StoreName");
        dataitemDetails = intent.getStringExtra("ItemDetails");
        imageView = intent.getStringExtra("img");

        Price = (TextView)findViewById(R.id.tvPrice);
        ItemDetails = findViewById(R.id.tvItemDetail);
        ItemName = findViewById(R.id.tvItemName);
        StoreName = findViewById(R.id.tvStoreName);


        Price.setText(dataprice);
        ItemDetails.setText(dataitemDetails);
        ItemName.setText(dataitemName);
        StoreName.setText(datastoreName);
    }
}
