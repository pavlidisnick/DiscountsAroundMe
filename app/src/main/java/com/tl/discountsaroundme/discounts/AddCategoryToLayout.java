package com.tl.discountsaroundme.discounts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.widget.Space;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.firebase_data.DiscountsManager;

public class AddCategoryToLayout {
    private LinearLayout linearLayout;
    private Activity activity;
    private DiscountsManager discountsManager;

    private int spaceWidth;
    private int buttonHeight;
    private int padding;

    public AddCategoryToLayout(LinearLayout linearLayout, Activity activity, DiscountsManager discountsManager) {
        this.linearLayout = linearLayout;
        this.activity = activity;
        this.discountsManager = discountsManager;
        buttonHeight = getPixelsFromDp(34);
        spaceWidth = getPixelsFromDp(6);
        padding = getPixelsFromDp(16);
    }

    /**
     * Add a button to the linearLayout you just passed to the constructor
     * Adds a space before adding the button
     */
    public void addCategory(final String buttonText) {
        LayoutInflater inflater = LayoutInflater.from(activity);

        @SuppressLint("InflateParams")
        Button button = (Button) inflater.inflate(R.layout.button_category_tag, null, false);

        button.setText(buttonText);
        button.setPadding(padding, 0, padding, 0);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discountsManager.getDiscountsByCategory(buttonText);
            }
        });
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, buttonHeight));

        addSpace();
        linearLayout.addView(button, linearLayout.getChildCount());
    }

    private void addSpace() {
        Space space = new Space(activity);
        space.setLayoutParams(new LinearLayout.LayoutParams(spaceWidth, LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.addView(space, linearLayout.getChildCount());
    }

    void clearCategories() {
        linearLayout.removeAllViews();
    }

    /**
     * Converts dp to pixels
     *
     * @return pixels
     */
    private int getPixelsFromDp(int dp) {
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }
}
