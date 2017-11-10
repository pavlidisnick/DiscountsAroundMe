package com.tl.discountsaroundme.FirebaseData;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.Entities.Store;
import com.tl.discountsaroundme.Entities.User;
import com.tl.discountsaroundme.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by rezu on 10/11/2017.
 */

public class UserInfoManager {

        public static void UserInformation(String UserUID , View rootview){
            final ImageButton IbtUserImage = rootview.findViewById(R.id.ibtEditImage);
            final TextView tvUserEmail = rootview.findViewById(R.id.tvUserEmail);
            final TextView tvUserType = rootview.findViewById(R.id.tvUserType);
            final TextView  tvUserDisplayName = rootview.findViewById(R.id.tvDisplayName);
            DatabaseReference mDbref = FirebaseDatabase.getInstance().getReference("/users");

            mDbref.child(UserUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    tvUserDisplayName.setText(user.getName());
                    tvUserType.setText(user.getUserType());
                    tvUserEmail.setText(user.getEmail());

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public static void ChangeDisplayName(){

        }

        public static void ChangeProfileImage(){

        }

}
