package com.tl.discountsaroundme.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.ShoppingCart;
import com.tl.discountsaroundme.entities.Item;
import com.tl.discountsaroundme.map.MarkerHelper;
import com.tl.discountsaroundme.ui_controllers.StatusBar;

public class ItemDetailsActivity extends Activity implements View.OnClickListener {
    private Item item;
    private Button addToCartButton;
    private ShoppingCart shoppingCart;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        new StatusBar(this);

        shoppingCart = new ShoppingCart(FirebaseDatabase.getInstance(), MainActivity.USER_ID);

        ImageView backImage = findViewById(R.id.back_button);
        backImage.setOnClickListener(this);

        addToCartButton = findViewById(R.id.add_to_cart_button);
        addToCartButton.setOnClickListener(this);

        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("ID");

        ImageView itemType = findViewById(R.id.storeImage);
        itemType.setImageDrawable(new MarkerHelper(this, null)
                .getDrawableByType(item.getType().toUpperCase()));

        if (item.isInCart()) {
            blueButtonWithCheck();
        }

        TextView price = findViewById(R.id.price);
        TextView itemDetails = findViewById(R.id.description);
        TextView itemName = findViewById(R.id.item);
        TextView storeName = findViewById(R.id.store);
        ImageView imageView = findViewById(R.id.imgItem);
        TextView type = findViewById(R.id.type);
        TextView discount = findViewById(R.id.correctPrice);

        price.setText("$" + item.getPrice());
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        discount.setText("$" + item.getFinalPrice());
        itemDetails.setText(item.getDescription());
        itemName.setText(item.getName());
        storeName.setText(item.getStore());
        type.append(item.getType());
        Glide.with(this)
                .load(item.getPicture())
                .into(imageView);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_button:
                this.finish();
                break;
            case R.id.add_to_cart_button:
                if (item.isInCart()) {
                    normalButtonWithCart();
                    shoppingCart.removeFromCart(item);
                } else {
                    blueButtonWithCheck();
                    shoppingCart.addToCart(item);
                }
        }
    }

    /**
     * Change the way the right button in the details looks to show that the item is added to the shopping cart
     * and it's ready to be removed
     */
    private void blueButtonWithCheck() {
        // icon change
        Drawable drawable = getResources().getDrawable(R.drawable.ic_done);
        int color = Color.parseColor("#FFFFFF");
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
        drawable.setColorFilter(color, mode);
        addToCartButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

        // background color change
        Drawable backgroundDrawable = getResources().getDrawable(R.drawable.button_main_style);
        int bgColor = Color.parseColor("#0080ff");
        PorterDuff.Mode bgMode = PorterDuff.Mode.SRC_ATOP;
        backgroundDrawable.setColorFilter(bgColor, bgMode);
        addToCartButton.setBackground(backgroundDrawable);

        addToCartButton.setText("Added to cart");
    }

    /**
     * Change the way the right button in the details looks to show that the item isn't in the shopping cart
     * and it's ready to be added
     */
    private void normalButtonWithCart() {
        // icon change
        Drawable drawable = getResources().getDrawable(R.drawable.ic_shopping_cart_add);
        int color = Color.parseColor("#FFFFFF");
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
        drawable.setColorFilter(color, mode);
        addToCartButton.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

        // background color change
        Drawable backgroundDrawable = getResources().getDrawable(R.drawable.button_main_style);
        addToCartButton.setBackground(backgroundDrawable);

        addToCartButton.setText("Add to cart");
    }
}
