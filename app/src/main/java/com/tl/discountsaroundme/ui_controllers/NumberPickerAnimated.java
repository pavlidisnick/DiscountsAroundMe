package com.tl.discountsaroundme.ui_controllers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.fragments.MapTab;

/**
 * NumberPickerAnimated
 * Set a seekBar listener to this object and it will show the progress with animations
 */
public class NumberPickerAnimated extends FrameLayout implements SeekBar.OnSeekBarChangeListener {

    LinearLayout linearLayout;

    // THE SHOWING TEXT VIEW
    TextView textView;

    View viewColorAnimate;

    private TextView textViewLeft;
    private TextView textViewRight;

    private int fontSize;

    private int number;

    private int red = 255;
    private int blue = 0;

    public NumberPickerAnimated(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLinearLayout(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberPickerAnimated, 0, 0);
        try {
            fontSize = a.getInteger(R.styleable.NumberPickerAnimated_fontSize, 150);

            number = a.getInteger(R.styleable.NumberPickerAnimated_startingNumber, 5);
            int sideTextSize = a.getInteger(R.styleable.NumberPickerAnimated_sideTextSize, 20);
            int sideTextPadding = a.getInteger(R.styleable.NumberPickerAnimated_sideTextPadding, 20);
            String leftText = a.getString(R.styleable.NumberPickerAnimated_leftText);
            String rightText = a.getString(R.styleable.NumberPickerAnimated_rightText);

            addLeftTextView(leftText, sideTextSize, sideTextPadding);
            setNumberTextView();
            addRightTextView(rightText, sideTextSize, sideTextPadding);
        } finally {
            a.recycle();
        }
    }

    private void setLinearLayout(Context context, AttributeSet attrs) {
        linearLayout = new LinearLayout(context, attrs);
        linearLayout.setGravity(Gravity.CENTER);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        linearLayout.setLayoutParams(lp);
        this.addView(linearLayout);
    }

    private void setNumberTextView() {
        textView = addTextView(fontSize);
        textView.setText(String.valueOf(number));
        linearLayout.addView(textView);
    }

    private void addLeftTextView(String text, int textSize, int sideTextPadding) {
        int padding = getPixelsFromDp(sideTextPadding);

        textViewLeft = addTextView(textSize);
        textViewLeft.setText(text);
        textViewLeft.setPadding(padding, 0, 0, 0);
        linearLayout.addView(textViewLeft);
    }

    private void addRightTextView(String text, int textSize, int sideTextPadding) {
        int padding = getPixelsFromDp(sideTextPadding);

        textViewRight = addTextView(textSize);
        textViewRight.setText(text);
        textViewRight.setPadding(0, 0, padding, 0);
        linearLayout.addView(textViewRight);
    }

    private void leftTextAnimationStart() {
        Animation hoursAnimation = new TranslateAnimation(0f, -40f, 0f, 0f);
        hoursAnimation.setDuration(400);
        hoursAnimation.setFillEnabled(true);
        hoursAnimation.setFillAfter(true);

        textViewLeft.startAnimation(hoursAnimation);
    }

    private void leftTextAnimationEnd() {
        Animation hoursAnimationReturn = new TranslateAnimation(-40f, 0f, 0f, 0f);
        hoursAnimationReturn.setDuration(400);
        hoursAnimationReturn.setFillEnabled(true);
        hoursAnimationReturn.setFillAfter(true);

        textViewLeft.startAnimation(hoursAnimationReturn);
    }

    private void rightTextAnimationStart() {
        final Animation hoursAnimation = new TranslateAnimation(0f, 40f, 0f, 0f);
        hoursAnimation.setDuration(400);
        hoursAnimation.setFillEnabled(true);
        hoursAnimation.setFillAfter(true);

        final Animation hoursAnimationReturn = new TranslateAnimation(40f, 0f, 0f, 0f);
        hoursAnimationReturn.setDuration(400);
        hoursAnimationReturn.setFillEnabled(true);
        hoursAnimationReturn.setFillAfter(true);

        textViewRight.startAnimation(hoursAnimation);
    }

    private void rightTextAnimationEnd() {
        final Animation hoursAnimationReturn = new TranslateAnimation(40f, 0f, 0f, 0f);
        hoursAnimationReturn.setDuration(400);
        hoursAnimationReturn.setFillEnabled(true);
        hoursAnimationReturn.setFillAfter(true);

        textViewRight.startAnimation(hoursAnimationReturn);
    }

    private TextView addTextView(int size) {
        TextView textView = new TextView(getContext());

        textView.setText(String.valueOf(number));
        textView.setTextSize(size);
        textView.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryWhite));

        LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        textView.setLayoutParams(lp);

        return textView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        YoYo.with(Techniques.Shake)
                .duration(300)
                .playOn(textView);

        textView.setText(String.valueOf(progress));

        try {
            if (number < progress)
                rightGradientChange();
            else if (number > progress)
                leftGradientChange();

            gradientChange();
        } catch (Exception e) {
            e.printStackTrace();
        }

        number = progress;
        MapTab.distance = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        leftTextAnimationStart();
        rightTextAnimationStart();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        leftTextAnimationEnd();
        rightTextAnimationEnd();
    }

    private int getPixelsFromDp(int dp) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public void setBackgroundToAnimate(View layout) {
        viewColorAnimate = layout;
    }

    @SuppressWarnings("deprecation")
    private void gradientChange() {
        // only red and blue changes
        String red = Integer.toHexString(this.red);
        String green = Integer.toHexString(107);
        String blue = Integer.toHexString(this.blue);
        String color = "#" + red + green + blue;

        int[] colors = {Color.parseColor(color), Color.parseColor("#f98abf")};

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);

        viewColorAnimate.setBackgroundDrawable(gd);
    }

    private void leftGradientChange() {
        if (blue > 0)
            blue -= 15;
        else if (red < 255)
            red += 15;
    }

    private void rightGradientChange() {
        if (blue < 255)
            blue += 15;
        else if (red > 0)
            red -= 15;
    }
}