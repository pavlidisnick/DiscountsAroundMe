package com.tl.discountsaroundme.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.Entities.User;
import com.tl.discountsaroundme.FirebaseData.UserInfoManager;
import com.tl.discountsaroundme.R;

public class UserTab extends Fragment {
    public static Boolean LogoutOnStop ;
    public ImageButton IbtUserImage;
    public TextView tvUserEmail;
    public TextView tvUserDisplayName;
    public TextView tvUserType;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    UserInfoManager userManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View  rootview = inflater.inflate(R.layout.tab_user_options, container, false);
        UserInfoManager.UserInformation(mAuth.getCurrentUser().getUid(),rootview);
        Switch swLogout =   rootview.findViewById(R.id.sLogoutOptions);
        final SharedPreferences mSharedprefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        swLogout.setChecked(mSharedprefs.getBoolean("logoutKey",false));
        swLogout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogoutOnStop = isChecked;

                SharedPreferences.Editor editor = mSharedprefs.edit();
                editor.putBoolean("logoutKey" ,LogoutOnStop);
                editor.commit();
            }
        });

        return rootview;
    }

}
