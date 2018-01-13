package com.tl.discountsaroundme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.UserPreferences;
import com.tl.discountsaroundme.ui_controllers.CheckBox;
import com.tl.discountsaroundme.ui_controllers.NumberPickerAnimated;

public class UserTab extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_user_options, container, false);

        View root_frame_layout = rootView.findViewById(R.id.root_frame_layout);

        NumberPickerAnimated numberPickerAnimated = rootView.findViewById(R.id.number_picker);
        numberPickerAnimated.setBackgroundToAnimate(root_frame_layout);

        SeekBar seekBar = rootView.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(numberPickerAnimated);


        CheckBox checkBox = rootView.findViewById(R.id.my_checkbox);


        return rootView;
    }
}
