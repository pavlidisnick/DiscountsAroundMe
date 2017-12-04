package com.tl.discountsaroundme.firebase_data;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.entities.User;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UserInfoManager {
    private User currentUser;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private DatabaseReference DbReference = FirebaseDatabase.getInstance().getReference();

    public UserInfoManager(FirebaseUser user, DatabaseReference dbRef) {
        this.user = user;
        this.dbRef = dbRef;
    }

    public void loadImage(final ImageView imageView) {
        dbRef.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUser = dataSnapshot.getValue(User.class);
                    Glide.with(getApplicationContext())
                            .load(currentUser.getImage())
                            .into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void changeEmail(final String email, String password) {
        userConfirmation(password);
        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    toastMessage("Email change successful");
                    dbRef.child("users").child(user.getUid()).child("email").setValue(email);
                } else {
                    toastMessage("Email change not successful Try again");
                }
            }
        });
    }

    public void changeDisplayName(final String displayName, String password) {
        userConfirmation(password);
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();
        user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    toastMessage("Your display name has changed!");
                    dbRef.child("users").child(user.getUid()).child("name").setValue(displayName);
                }
            }
        });

    }

    public void changePassword(String newPassword, String oldPassword) {
        userConfirmation(oldPassword);
        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    toastMessage("Password Change complete!");
                }
            }
        });
    }

    public void deleteAccount(String Password) {
        userConfirmation(Password);
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    toastMessage("Account Deleted");
                    dbRef.child("users").child(user.getUid()).removeValue();
                    dbRef.child("shops").child(user.getUid()).removeValue();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                } else {
                    toastMessage("Account Delete Fail");
                }
            }
        });
    }

    private void userConfirmation(String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void changeUserPicture(String imageString) {
        dbRef.child("users").child(user.getUid()).child("image").setValue(imageString);
    }
    public void StoreUserToDB(boolean isOwner){
        String userType = "Customer";
        if (isOwner) {
            userType = "Store owner";
        }
        User newUser = new User(user.getEmail(), user.getEmail(), userType, "imgURL");
        dbRef.child("users").child(user.getUid()).setValue(newUser);
        toastMessage("Register successful. Welcome " + user.getEmail());
    }
}
