package com.tl.discountsaroundme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.ui_controllers.NumberPickerAnimated;

public class UserTab extends Fragment {
    private boolean notifyEveryHour = false;
    public static int notifyEvery = 5;

    private LinearLayout linearLayout;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_user_options, container, false);

        final TextView everyText = rootView.findViewById(R.id.everyText);
        final TextView numberDisplay = rootView.findViewById(R.id.hours_text_view);
        final TextView hoursText = rootView.findViewById(R.id.hoursText);

        final ImageView checkBox = rootView.findViewById(R.id.checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notifyEveryHour) {
                    checkBox.setImageResource(R.drawable.checkbox_unchecked);
                    notifyEveryHour = false;
                } else {
                    checkBox.setImageResource(R.drawable.checkbox);
                    notifyEveryHour = true;
                }
            }
        });

        // Animations for texts every and hour
        final Animation everyAnimation = new TranslateAnimation(0f, -40f, 0f, 0f);
        everyAnimation.setDuration(400);
        everyAnimation.setFillEnabled(true);
        everyAnimation.setFillAfter(true);

        final Animation everyAnimationReturn = new TranslateAnimation(-40f, 0f, 0f, 0f);
        everyAnimationReturn.setDuration(400);
        everyAnimationReturn.setFillEnabled(true);
        everyAnimationReturn.setFillAfter(true);

        final Animation hoursAnimation = new TranslateAnimation(0f, 40f, 0f, 0f);
        hoursAnimation.setDuration(400);
        hoursAnimation.setFillEnabled(true);
        hoursAnimation.setFillAfter(true);

        final Animation hoursAnimationReturn = new TranslateAnimation(40f, 0f, 0f, 0f);
        hoursAnimationReturn.setDuration(400);
        hoursAnimationReturn.setFillEnabled(true);
        hoursAnimationReturn.setFillAfter(true);

        final NumberPickerAnimated numberPickerAnimated = rootView.findViewById(R.id.number_picker);

        SeekBar seekBar = rootView.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(numberPickerAnimated);

//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                everyText.startAnimation(everyAnimation);
//                hoursText.startAnimation(hoursAnimation);
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                everyAnimation.cancel();
//                hoursAnimation.cancel();
//                everyText.startAnimation(everyAnimationReturn);
//                hoursText.startAnimation(hoursAnimationReturn);
//            }
//        });

        return rootView;
    }

    private void animateNumberLeft() {

    }

    private void animateNumberRight() {

    }

    private void animationToLeft() {

    }

    private void animationToRight() {

    }

}
