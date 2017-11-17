package com.tl.discountsaroundme.FirebaseData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.Activities.UserProfileActivity;
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
    static DatabaseReference mDbref = FirebaseDatabase.getInstance().getReference();
    public static User Currentuser;

    public static void UserInformation(String UserUID, View rootview) {
        final TextView tvUserEmail = rootview.findViewById(R.id.tvUserEmail);
        final TextView tvUserType = rootview.findViewById(R.id.tvUserType);
        final TextView tvUserDisplayName = rootview.findViewById(R.id.tvDisplayName);
        /**
         * Using the current user's UID get the user Account details from firebase. And using the rootview set the correct values to the UI
         */
        mDbref.child("users").child(UserUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    tvUserDisplayName.setText(user.getName());
                    tvUserType.setText(user.getUserType());
                    tvUserEmail.setText(user.getEmail());
                    Currentuser = user;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void ChangeEmail(final FirebaseUser user, final String Email, String Password) {
        UserReauthentication(user, Password);
        user.updateEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ToastMessage("Email change successful");
                    mDbref.child("users").child(user.getUid()).child("email").setValue(Email);
                } else {
                    ToastMessage("Email change not successful Try again");
                }
            }
        });
    }

    public static void ChangeDisplayName(final FirebaseUser user, final String DisplayName) {
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(DisplayName)
                .build();
        user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ToastMessage("Your display name has changed!");
                    mDbref.child("users").child(user.getUid()).child("name").setValue(DisplayName);
                }
            }
        });

    }

    public static void ChangeProfileImage() {

    }

    public static void ChangePassword(FirebaseUser user, String newPassword, String oldPassword) {
        UserReauthentication(user, oldPassword);
        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ToastMessage("Password Change complete!");
                        }
                    }
                });
    }

    public static void UserReauthentication(FirebaseUser user, String Password) {
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), Password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ToastMessage("User Re-Authenticated");
                    }
                });
    }

    public static void DeleteAccount(final FirebaseUser user, String Password) {
        UserReauthentication(user, Password);
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ToastMessage("Account Deleted");
                    mDbref.child("users").child(user.getUid()).removeValue();
                    mDbref.child("shops").child(user.getUid()).removeValue();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                } else {
                    ToastMessage("Account Delete Fail");
                }
            }
        });
    }

    public static void ToastMessage(String Message) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, Message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
