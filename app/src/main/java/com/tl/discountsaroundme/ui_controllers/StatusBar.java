package com.tl.discountsaroundme.ui_controllers;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Window;
import android.view.WindowManager;

import com.tl.discountsaroundme.R;

public class StatusBar {

    public StatusBar(Activity activity) {
        Window window = activity.getWindow();
        Drawable background = activity.getResources().getDrawable(R.drawable.blue_gradient);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(R.color.transparent));
        window.setNavigationBarColor(activity.getResources().getColor(R.color.transparent));
        window.setBackgroundDrawable(background);
    }
}
