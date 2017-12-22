package com.tl.discountsaroundme.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.entities.Item;
import com.tl.discountsaroundme.ui_controllers.ItemViewAdapter;
import com.tl.discountsaroundme.ui_controllers.StatusBar;

public class ItemDetailsActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        new StatusBar(this);

        ImageView backImage = findViewById(R.id.back_button);
        backImage.setOnClickListener(this);

        Intent intent = getIntent();
        String dataPrice = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_PRICE);
        String dataItemName = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_NAME);
        String dataStoreName = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_STORE);
        String dataItemDetails = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_DETAILS);
        String dataImg = intent.getStringExtra(ItemViewAdapter.DATA_IMAGE);
        String dataType = intent.getStringExtra(ItemViewAdapter.DATA_TYPE);
        String dataDiscount = intent.getStringExtra(ItemViewAdapter.DATA_DISCOUNT);

        TextView price = findViewById(R.id.price);
        TextView itemDetails = findViewById(R.id.description);
        TextView itemName = findViewById(R.id.item);
        TextView storeName = findViewById(R.id.store);
        ImageView imageView = findViewById(R.id.imgItem);
        TextView type = findViewById(R.id.type);
        TextView discount = findViewById(R.id.correctPrice);

        price.setText(dataPrice);
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        discount.setText(new Item().getFinalPrice(dataPrice, dataDiscount));
        itemDetails.setText(dataItemDetails);
        itemName.setText(dataItemName);
        storeName.setText(dataStoreName);
        type.append(dataType);
        Glide.with(this)
                .load(dataImg)
                .into(imageView);
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}
