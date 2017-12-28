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
import com.tl.discountsaroundme.entities.Item;

public class StoreItemDetailsActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_item_details);

        Intent intent = getIntent();
        final Item item = (Item) intent.getSerializableExtra("ITEM");

        TextView price = findViewById(R.id.price2);
        TextView itemDetails = findViewById(R.id.description2);
        TextView itemName = findViewById(R.id.item2);
        TextView storeName = findViewById(R.id.store2);
        ImageView imageView = findViewById(R.id.imgItem2);
        TextView type = findViewById(R.id.type2);
        TextView discount = findViewById(R.id.correctPrice2);

        price.setText(String.valueOf(item.getPrice()));
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        discount.setText(item.getFinalPrice());
        itemDetails.setText(item.getDescription());
        itemName.setText(item.getName());
        storeName.setText(item.getStore());
        type.append(item.getType());
        Glide.with(this)
                .load(item.getPicture())
                .into(imageView);

        Button button = findViewById(R.id.deleteButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDiscount(item);
                onBackPressed();
            }
        });
    }

    private void deleteDiscount(Item item) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("items");
        ref.child(item.getId()).removeValue();
    }
}
