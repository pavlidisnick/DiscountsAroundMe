package com.tl.discountsaroundme.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.ui_controllers.ItemViewAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ItemDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

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
        discount.setText(getFinalPrice(dataPrice, dataDiscount));
        itemDetails.append(dataItemDetails);
        itemName.setText(dataItemName);
        storeName.append(dataStoreName);
        type.append(dataType);
        Glide.with(this)
                .load(dataImg)
                .into(imageView);
    }

    private String getFinalPrice(String priceString, String discountString) {
        priceString = priceString.replace("$", "").trim();
        double price = Double.parseDouble(priceString);
        double discount = Double.parseDouble(discountString);
        BigDecimal finalPrice = BigDecimal.valueOf(price - (price * discount / 100)).setScale(2, RoundingMode.HALF_UP);
        return String.valueOf(finalPrice).concat("$");
    }
}
