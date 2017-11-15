package com.tl.discountsaroundme.FirebaseData;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
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

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by rezu on 10/11/2017.
 */

public class UserInfoManager {


    public static void UserInformation(String UserUID, View rootview) {
        final TextView tvUserEmail = rootview.findViewById(R.id.tvUserEmail);
        final TextView tvUserType = rootview.findViewById(R.id.tvUserType);
        final TextView tvUserDisplayName = rootview.findViewById(R.id.tvDisplayName);
        DatabaseReference mDbref = FirebaseDatabase.getInstance().getReference("/users");
        /**
         * Using the current user's UID get the user Account details from firebase. And using the rootview set the correct values to the UI
         * TODO:  simplify the class  using more methods.
         */
        mDbref.child(UserUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    tvUserDisplayName.setText(user.getName());
                    tvUserType.setText(user.getUserType());
                    tvUserEmail.setText(user.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void ChangeEmail(FirebaseUser user, String Email) {
        user.updateEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                int duration = Toast.LENGTH_SHORT;
                Context context = getApplicationContext();
                String result = "";
                Toast toast = Toast.makeText(context, result, duration);
                if (task.isSuccessful()) {
                    result = "Email change successful";
                    toast.show();
                } else {
                    result = "ERROR email change not successful";
                }
            }
        });
    }

    public static void ChangeDisplayName() {

    }

    public static void ChangeProfileImage() {

    }

}
