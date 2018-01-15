package com.tl.discountsaroundme.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.UserPreferences;
import com.tl.discountsaroundme.ui_controllers.CheckBox;
import com.tl.discountsaroundme.ui_controllers.NumberPickerAnimated;

import static com.tl.discountsaroundme.fragments.MapTab.isNotificationsEnabled;
import static com.tl.discountsaroundme.fragments.MapTab.notifyEvery;

public class UserTab extends Fragment implements CheckBox.MyOnCheckListener, NumberPickerAnimated.MyOnSeekChangeListener {

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_user_options, container, false);

        View root_frame_layout = rootView.findViewById(R.id.root_frame_layout);

        SeekBar seekBar = rootView.findViewById(R.id.seekBar);

        NumberPickerAnimated numberPickerAnimated = rootView.findViewById(R.id.number_picker);
        numberPickerAnimated.setBackgroundToAnimate(root_frame_layout);
        numberPickerAnimated.setSeekBar(seekBar);
        numberPickerAnimated.setOnSeekChangeListener(this);

        CheckBox checkBox = rootView.findViewById(R.id.my_checkbox);
        checkBox.setOnCheckedChangeListener(this);

        return rootView;
    }

    @Override
    public void onChange(CheckBox view, boolean checked) {
        isNotificationsEnabled = checked;
        UserPreferences.saveDataBool("NearbyOffersCheck", checked);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        notifyEvery = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
