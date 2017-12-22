package com.tl.discountsaroundme.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.ui_controllers.ItemViewAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StoreItemDetailsActivity extends Activity {

    private String key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_item_details);

        Intent intent = getIntent();
        String dataPrice = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_PRICE);
        String dataItemName = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_NAME);
        String dataStoreName = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_STORE);
        String dataItemDetails = intent.getStringExtra(ItemViewAdapter.DATA_ITEM_DETAILS);
        String dataImg = intent.getStringExtra(ItemViewAdapter.DATA_IMAGE);
        String dataType = intent.getStringExtra(ItemViewAdapter.DATA_TYPE);
        String dataDiscount = intent.getStringExtra(ItemViewAdapter.DATA_DISCOUNT);

        key = intent.getStringExtra("KEY");

        TextView price = findViewById(R.id.price2);
        TextView itemDetails = findViewById(R.id.description2);
        TextView itemName = findViewById(R.id.item2);
        TextView storeName = findViewById(R.id.store2);
        ImageView imageView = findViewById(R.id.imgItem2);
        TextView type = findViewById(R.id.type2);
        TextView discount = findViewById(R.id.correctPrice2);

        price.setText(dataPrice);
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        discount.setText(getFinalPrice(dataPrice, dataDiscount));
        itemDetails.setText(dataItemDetails);
        itemName.setText(dataItemName);
        storeName.setText(dataStoreName);
        type.append(dataType);
        Glide.with(this)
                .load(dataImg)
                .into(imageView);

        Button button = findViewById(R.id.deleteButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDiscount();
                onBackPressed();
            }
        });
    }

    private String getFinalPrice(String priceString, String discountString) {
        priceString = priceString.replace("$", "").trim();
        double price = Double.parseDouble(priceString);
        double discount = Double.parseDouble(discountString);
        BigDecimal finalPrice = BigDecimal.valueOf(price - (price * discount / 100)).setScale(2, RoundingMode.HALF_UP);
        return "$" + String.valueOf(finalPrice);
    }

    private void deleteDiscount(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("items");
        ref.child(key).removeValue();
    }
}
