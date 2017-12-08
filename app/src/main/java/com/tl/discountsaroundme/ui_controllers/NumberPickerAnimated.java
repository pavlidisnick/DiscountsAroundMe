package com.tl.discountsaroundme.ui_controllers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tl.discountsaroundme.R;

import java.util.ArrayList;
import java.util.List;

public class NumberPickerAnimated extends FrameLayout implements SeekBar.OnSeekBarChangeListener, Animation.AnimationListener {
    private int size = 150;

    private int number = 5;
    private int maxNumber = 24;

    // ANIMATION DURATIONS
    private int animationDuration = 300;

    // ALL TEXT VIEWS
    private List<TextView> textViewList = new ArrayList<>();

    // THE SHOWING TEXT VIEW
    TextView textView;

    public NumberPickerAnimated(Context context, AttributeSet attrs) {
        super(context, attrs);

        fillList();
    }

    boolean isInvokedLeft = false;
    boolean isInvokedRight = false;

    float x;
    float absoluteCenter;
    float endX;

    float positionLeftShowing;
    float positionLeft;

    float positionRightShowing;
    float positionRight;

    private void initialize() {
        // to place the showing text view
        // also start fading out from here
        x = textView.getX();
        // absolute center of layout
        absoluteCenter = x + textView.getWidth() / 2;

        // start fading out from here
        endX = x + textView.getWidth();

        // position to start viewing text views to go left to center
        positionLeftShowing = (float) (x - (textView.getWidth() * 0.8));

        // position text view to go left to center
        positionLeft = x - textView.getWidth();

        // position text views to go center to right
        positionRightShowing = (float) (endX + (textView.getWidth() * 0.8));

        // position text  view to go center to right
        positionRight = endX + textView.getWidth();
    }

    private void fillList() {
        for (int i = 0; i <= maxNumber; i++) {
            TextView textView = addTextView(size);
            textView.setVisibility(GONE);
            textView.setText(String.valueOf(i));
            textViewList.add(textView);

            if (i == number) {
                textView.setVisibility(VISIBLE);
                this.textView = textView;
            }
        }
    }

    private TextView addTextView(int size) {
        TextView textView = new TextView(getContext());

        textView.setText(String.valueOf(number));
        textView.setTextSize(size);
        textView.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryWhite));

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        textView.setLayoutParams(lp);

        this.addView(textView);
        return textView;
    }

    public void animateLeft() {
        if (!isInvokedLeft && !isInvokedRight) {
            int indexOfCurrent = textViewList.indexOf(textView);

            if (indexOfCurrent != 0) {
                TextView oldTextView = textView;
                TextView textView = textViewList.get(indexOfCurrent - 1);

                animateCenterToRight(oldTextView);
                animateLeftToCenter(textView);

                this.textView = textView;

                isInvokedLeft = true;
            }
        }
    }

    public void animateRight() {
        if (!isInvokedRight && !isInvokedLeft) {
            int indexOfCurrent = textViewList.indexOf(textView);

            if (indexOfCurrent != textViewList.size() - 1) {
                TextView oldTextView = textView;
                TextView textView = textViewList.get(indexOfCurrent + 1);

                animateCenterToLeft(oldTextView);
                animateRightToCenter(textView);

                this.textView = textView;

                isInvokedRight = true;
            }
        }
    }


    // ANIMATIONS


    private void animateLeftToCenter(final TextView textView) {
        textView.setX(positionLeft);
        textView.setVisibility(VISIBLE);

        AnimationSet animationSet = new AnimationSet(true);

        TranslateAnimation translateLeft =
                new TranslateAnimation(positionLeft, x, 0f, 0f);
        translateLeft.setFillBefore(true);
        translateLeft.setFillEnabled(true);
        translateLeft.setFillAfter(true);
        translateLeft.setDuration(animationDuration);

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setFillAfter(true);
        fadeIn.setDuration(animationDuration);

        ScaleAnimation scaleAnim = new ScaleAnimation(1.5f, 1.0f, 1.5f,
                1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(animationDuration);

        animationSet.addAnimation(translateLeft);
        animationSet.addAnimation(fadeIn);
        animationSet.addAnimation(scaleAnim);

        animationSet.setDuration(animationDuration);
        animationSet.setFillAfter(true);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.setX(x);

                isInvokedRight = false;
                isInvokedLeft = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        textView.startAnimation(animationSet);
    }


    // CENTER TO LEFT

    private void animateCenterToLeft(final TextView textView) {
        textView.setX(x);
        textView.setVisibility(VISIBLE);

        AnimationSet animationSet = new AnimationSet(true);

        TranslateAnimation translateLeft =
                new TranslateAnimation(x, positionLeft, 0f, 0f);
        translateLeft.setFillBefore(true);
        translateLeft.setFillEnabled(true);
        translateLeft.setFillAfter(true);
        translateLeft.setDuration(animationDuration);

        Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setFillAfter(true);
        fadeOut.setDuration(animationDuration);

        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.5f, 1.0f,
                1.5f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(animationDuration);

        animationSet.addAnimation(translateLeft);
        animationSet.addAnimation(fadeOut);
        animationSet.addAnimation(scaleAnim);

        animationSet.setDuration(animationDuration);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.setText(GONE);

                isInvokedLeft = false;
                isInvokedRight = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        textView.startAnimation(animationSet);
    }


    // CENTER TO RIGHT

    private void animateCenterToRight(final TextView textView) {
        textView.setX(x);
        textView.setVisibility(VISIBLE);

        AnimationSet animationSet = new AnimationSet(true);

        TranslateAnimation translateLeft =
                new TranslateAnimation(x, positionRight, 0f, 0f);
        translateLeft.setFillBefore(true);
        translateLeft.setFillEnabled(true);
        translateLeft.setFillAfter(true);
        translateLeft.setDuration(animationDuration);

        Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setFillAfter(true);
        fadeOut.setDuration(animationDuration);

        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.75f, 1.0f,
                0.75f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(animationDuration);

        animationSet.addAnimation(translateLeft);
        animationSet.addAnimation(fadeOut);
        animationSet.addAnimation(scaleAnim);

        animationSet.setDuration(animationDuration);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.setText(GONE);

                isInvokedRight = false;
                isInvokedLeft = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        textView.startAnimation(animationSet);
    }

    // RIGHT TO CENTER

    private void animateRightToCenter(final TextView textView) {
        textView.setX(positionRight);
        textView.setVisibility(VISIBLE);

        AnimationSet animationSet = new AnimationSet(true);

        TranslateAnimation translateLeft =
                new TranslateAnimation(positionRight, x, 0f, 0f);
        translateLeft.setFillBefore(true);
        translateLeft.setFillEnabled(true);
        translateLeft.setFillAfter(true);
        translateLeft.setDuration(animationDuration);

        Animation fadeOut = new AlphaAnimation(0.0f, 1.0f);
        fadeOut.setFillAfter(true);
        fadeOut.setDuration(animationDuration);

        ScaleAnimation scaleAnim = new ScaleAnimation(0.75f, 1.0f, 0.75f,
                1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(animationDuration);

        animationSet.addAnimation(translateLeft);
        animationSet.addAnimation(fadeOut);
        animationSet.addAnimation(scaleAnim);

        animationSet.setDuration(animationDuration);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.setX(x);

                isInvokedRight = false;
                isInvokedLeft = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        textView.startAnimation(animationSet);
    }


    // END ANIMATIONS


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progress < number) {
            number = progress;
            animateLeft();
        } else {
            number = progress;
            animateRight();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        initialize();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
