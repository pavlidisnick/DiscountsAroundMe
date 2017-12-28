package com.tl.discountsaroundme.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.firebase_data.UserInfoManager;
import com.tl.discountsaroundme.ui_controllers.StatusBar;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Button btMailChange, btPassChange, btImageChange, btDisplayName, btDeleteAcc;
    TextView tvUserDisplayName;
    ImageView imageView;
    UserInfoManager userInfoManager;
    Uri imageUri;
    String[] userInput = new String[2];

    private static final int SELECTED_PICTURE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        new StatusBar(this);

        ImageView backImage = findViewById(R.id.back_button);
        backImage.setOnClickListener(this);

        imageView = findViewById(R.id.Image);

        tvUserDisplayName = findViewById(R.id.tvDisplayName);

        btImageChange = findViewById(R.id.btImageChange);
        btMailChange = findViewById(R.id.btMailChange);
        btPassChange = findViewById(R.id.btPassChange);
        btDisplayName = findViewById(R.id.btDisplayName);
        btDeleteAcc = findViewById(R.id.btDeleteAccount);

        btImageChange.setOnClickListener(this);
        btMailChange.setOnClickListener(this);
        btPassChange.setOnClickListener(this);
        btDisplayName.setOnClickListener(this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            userInfoManager = new UserInfoManager(user, FirebaseDatabase.getInstance().getReference());
            userInfoManager.loadImage(imageView);
            tvUserDisplayName.setText(user.getDisplayName());
        } else {
            btImageChange.setEnabled(false);
            btMailChange.setEnabled(false);
            btPassChange.setEnabled(false);
            btDisplayName.setEnabled(false);
            btDeleteAcc.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btImageChange:
                openGalery();
                break;
            case R.id.btMailChange:
                createDialog(btMailChange.getText().toString(), "Email", "Password confirmation",
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (userInput[0] != null)
                                    userInfoManager.changeEmail(userInput[0], userInput[1]);
                            }
                        });
                break;
            case R.id.btPassChange:
                createDialog(btPassChange.getText().toString(), "New Password", "Old password",
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (userInput[0] != null)
                                    userInfoManager.changePassword(userInput[0], userInput[1]);
                            }
                        });
                break;
            case R.id.btDisplayName:
                createDialog(btDisplayName.getText().toString(), "New Name", "Password confirmation",
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (userInput[0] != null)
                                    userInfoManager.changeDisplayName(userInput[0], userInput[1]);
                            }
                        });
                break;
            case R.id.btDeleteAccount:
                createDialog(btDeleteAcc.getText().toString(), "Password", "Confirm Password",
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (userInput[0] != null && userInput[0].equals(userInput[1]))
                                    userInfoManager.deleteAccount(userInput[0]);
                            }
                        });
                break;
            case R.id.back_button:
                this.finish();
                break;
        }
    }

    private void openGalery() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to change your Image?")
                .setTitle("Alert Edit");
// Add the buttons
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECTED_PICTURE);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.show();


        }


    private void createDialog(String title, String hint1, String hint2, DialogInterface.OnDismissListener dismissListener) {
        @SuppressLint("InflateParams")
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.profile_dialog, null);
        final EditText editText1 = (EditText) linearLayout.getChildAt(0);
        final EditText editText2 = (EditText) linearLayout.getChildAt(1);
        editText1.setHint(hint1);
        editText2.setHint(hint2);

        new AlertDialog.Builder(this).setView(linearLayout)
                .setTitle(title)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        userInput[0] = editText1.getText().toString();
                        userInput[1] = editText2.getText().toString();
                    }
                })
                .setOnDismissListener(dismissListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SELECTED_PICTURE) {
            imageUri = data.getData();

            imageView.setImageURI(imageUri);


        }
    }
}
