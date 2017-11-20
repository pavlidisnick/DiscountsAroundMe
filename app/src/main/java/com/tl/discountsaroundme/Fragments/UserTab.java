package com.tl.discountsaroundme.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.tl.discountsaroundme.Activities.MainActivity;
import com.tl.discountsaroundme.Activities.UserProfileActivity;
import com.tl.discountsaroundme.FirebaseData.UserInfoManager;
import com.tl.discountsaroundme.R;

public class UserTab extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    public static Boolean LogoutOnStop;
    Switch swLogout;
    Button btEditUser;
    Button btApply;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_user_options, container, false);

        // #47 Set a switch on changed variable to be stored in the SharedPreferences regarding the  user's preference on logout option.
        swLogout = rootView.findViewById(R.id.sLogoutOptions);
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        swLogout.setChecked(mSharedPrefs.getBoolean("logoutKey", false));
        btEditUser = rootView.findViewById(R.id.btEditUser);
        btApply = rootView.findViewById(R.id.btApply);

        btEditUser.setOnClickListener(this);
        btApply.setOnClickListener(this);
        swLogout.setOnCheckedChangeListener(this);

        return rootView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO: make it work properly
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        LogoutOnStop = isChecked;
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putBoolean("logoutKey", LogoutOnStop);
        editor.commit();
    }


    @Override
    public void onClick(View v) {
        if (v.equals(btEditUser)) {
            Intent UserProfileActivity = new Intent(getActivity(), UserProfileActivity.class);
            startActivity(UserProfileActivity);
        } else if (v.equals(btApply)) {
            //APPLY USER PREFS
        }
    }

}
