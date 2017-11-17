package com.tl.discountsaroundme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tl.discountsaroundme.UiControllers.ItemViewAdapter;

public class ItemDetailsActivity extends Activity {

    String dataPrice, dataItemName, dataItemDetails, dataStoreName, dataImg, dataType, dataDiscount;

    TextView price;
    TextView itemName;
    TextView itemDetails;
    TextView storeName;
    ImageView imageView;
    TextView type;
    TextView discount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Intent intent = getIntent();
        dataPrice = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_PRICE);
        dataItemName = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_NAME);
        dataStoreName = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_STORE);
        dataItemDetails = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_DETAILS);
        dataImg = intent.getStringExtra(ItemViewAdapter.DATA_IMAGE);
        dataType = intent.getStringExtra(ItemViewAdapter.DATA_TYPE);
        dataDiscount = intent.getStringExtra(ItemViewAdapter.DATA_DISCOUNT);

        price = findViewById(R.id.price);
        itemDetails = findViewById(R.id.description);
        itemName = findViewById(R.id.item);
        storeName = findViewById(R.id.store);
        imageView = findViewById(R.id.imgItem);
        type = findViewById(R.id.type);
        discount = findViewById(R.id.correctPrice);

        price.setText(" " + dataPrice);
        itemDetails.append(dataItemDetails);
        itemName.setText(dataItemName);
        storeName.append(dataStoreName);
        type.append(dataType);
        discount.setText(dataDiscount+"%");
        Glide.with(this)
                .load(dataImg)
                .into(imageView);
    }
}
