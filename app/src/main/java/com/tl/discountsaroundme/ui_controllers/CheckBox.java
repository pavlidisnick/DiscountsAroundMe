package com.tl.discountsaroundme.ui_controllers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tl.discountsaroundme.R;

public class CheckBox extends FrameLayout implements View.OnClickListener, Checkable {
    private ImageView background;
    private ImageView circle;
    private boolean isChecked = true;
    private OnCheckedChangeListener onCheckedListener;

    public CheckBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addImageView();
        addImageViewCircle();
    }

    private void addImageView() {
        background = new ImageView(getContext());
        background.setImageResource(R.drawable.checkbox);
        background.setOnClickListener(this);
        LayoutParams lp = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        background.setLayoutParams(lp);

        this.addView(background);
    }

    private void addImageViewCircle() {
        circle = new ImageView(getContext());
        circle.setImageResource(R.drawable.circle);
        circle.setOnClickListener(this);
        LayoutParams lp = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        circle.setLayoutParams(lp);

        this.addView(circle);
    }

    @Override
    public void onClick(View v) {
        if (isChecked) {
            background.setImageResource(R.drawable.checkbox_unchecked);
            isChecked = false;
            animateCircleLeft();
        } else {
            animateCircleRight();
            background.setImageResource(R.drawable.checkbox);
            isChecked = true;
        }
    }

    private void animateCircleLeft() {
        final float toGo = -this.getWidth() / 2 + 12;

        TranslateAnimation translateAnimation = new TranslateAnimation(0f, toGo, 0f, 0f);
        translateAnimation.setDuration(200);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setFillAfter(true);

        circle.startAnimation(translateAnimation);
    }

    private void animateCircleRight() {
        final float toGo = -this.getWidth() / 2 + 12;

        TranslateAnimation translateAnimation = new TranslateAnimation(toGo, 0f, 0f, 0f);
        translateAnimation.setDuration(200);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setFillAfter(true);

        circle.startAnimation(translateAnimation);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        onCheckedListener.onChange(this, isChecked);
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedListener = listener;
    }

    public interface OnCheckedChangeListener {
        void onChange(CheckBox view, boolean checked);
    }
}
