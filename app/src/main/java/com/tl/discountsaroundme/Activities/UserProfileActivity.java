package com.tl.discountsaroundme.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tl.discountsaroundme.FirebaseData.UserInfoManager;
import com.tl.discountsaroundme.R;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static int RESULT_LOAD_IMAGE;
    EditText etChangeEmail, etChangePass, etChoice, etPassword, etDisplayName;
    Button btOk, btMailChange, btPassChange, btImageChange, btDisplayName, btDeleteAcc, btYes, btNo;
    TextView tvUserDisplayName, tvDeleteAccount;
    ImageView Image;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Image = findViewById(R.id.Image);
        tvDeleteAccount = findViewById(R.id.tvDelAcc);
        tvUserDisplayName = findViewById(R.id.tvDisplayName);
        tvUserDisplayName.setText(UserInfoManager.Currentuser.getName());
        btDeleteAcc = findViewById(R.id.btDeleteAccount);
        btYes = findViewById(R.id.btYes);
        btNo = findViewById(R.id.btNo);
        btDisplayName = findViewById(R.id.btDisplayName);
        btImageChange = findViewById(R.id.btImageChange);
        btMailChange = findViewById(R.id.btMailChange);
        btPassChange = findViewById(R.id.btPassChange);
        btOk = findViewById(R.id.btOk);
        etChangeEmail = findViewById(R.id.etChangeEmail);
        etChangePass = findViewById(R.id.etChangePass);
        etPassword = findViewById(R.id.etPassword);
        etDisplayName = findViewById(R.id.etDisplayName);
        btImageChange.setOnClickListener(this);
        btMailChange.setOnClickListener(this);
        btPassChange.setOnClickListener(this);
        btOk.setOnClickListener(this);
        btDeleteAcc.setOnClickListener(this);
        btDisplayName.setOnClickListener(this);
        btYes.setOnClickListener(this);
        btNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ClearLayout();
        btOk.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.btPassChange:
                LayoutChange(etChangePass);
                break;
            case R.id.btImageChange:
                //TODO Change image
                break;
            case R.id.btMailChange:
                LayoutChange(etChangeEmail);
                break;
            case R.id.btDeleteAccount:
                DeleteAccountLayoutChange();
                break;
            case R.id.btYes:
                DeleteAccount();
                DeleteAccountCancel();
                break;
            case R.id.btNo:
                DeleteAccountCancel();
                break;
            case R.id.btDisplayName:
                LayoutChange(etDisplayName);
                break;
            case R.id.btOk:
                ChangeAction();
                break;
        }
    }

    public void LayoutChange(EditText etView) {
        ClearLayout();
        if (etView.equals(etChangeEmail)) {
            etView = findViewById(R.id.etChangeEmail);
        } else if (etView.equals(etChangePass)) {
            etView = findViewById(R.id.etChangePass);
        } else if (etView.equals(etDisplayName)) {
            etView = findViewById(R.id.etDisplayName);
        }
        etPassword.setVisibility(View.VISIBLE);
        etView.setVisibility(View.VISIBLE);
        btOk.setVisibility(View.VISIBLE);
        etChoice = etView;
    }

    private void ChangeAction() {
        if (etChoice.equals(etChangeEmail)) {
            String newEmail = etChoice.getText().toString();
            String Password = etPassword.getText().toString();
            UserInfoManager.ChangeEmail(user, newEmail, Password);
        } else if (etChoice.equals(etChangePass)) {
            String oldPassword = etPassword.getText().toString();
            String Password = etChoice.getText().toString();
            UserInfoManager.ChangePassword(user, oldPassword, Password);
        } else if (etChoice.equals(etDisplayName)) {
            String newDisplayName = etChoice.getText().toString();
            UserInfoManager.ChangeDisplayName(user, newDisplayName);
        }
        etChoice.getText().clear();
        etPassword.getText().clear();
        ClearLayout();
        btOk.setVisibility(View.GONE);
    }

    public void ClearLayout() {
        etDisplayName.setVisibility(View.GONE);
        etChangeEmail.setVisibility(View.GONE);
        etChangePass.setVisibility(View.GONE);
        etPassword.setVisibility(View.GONE);
    }

    public void DeleteAccountLayoutChange() {
        btDisplayName.setVisibility(View.GONE);
        btMailChange.setVisibility(View.GONE);
        btImageChange.setVisibility(View.GONE);
        btPassChange.setVisibility(View.GONE);
        btDeleteAcc.setVisibility(View.GONE);
        etPassword.setVisibility(View.VISIBLE);
        btYes.setVisibility(View.VISIBLE);
        btNo.setVisibility(View.VISIBLE);
        tvDeleteAccount.setVisibility(View.VISIBLE);
    }

    public void DeleteAccountCancel() {
        btDisplayName.setVisibility(View.VISIBLE);
        btMailChange.setVisibility(View.VISIBLE);
        btImageChange.setVisibility(View.VISIBLE);
        btPassChange.setVisibility(View.VISIBLE);
        btDeleteAcc.setVisibility(View.VISIBLE);
        btDeleteAcc.setVisibility(View.VISIBLE);
        btYes.setVisibility(View.GONE);
        btNo.setVisibility(View.GONE);
        tvDeleteAccount.setVisibility(View.GONE);
    }

    public void DeleteAccount() {
        String password = etPassword.getText().toString();
        UserInfoManager.DeleteAccount(user, password);
    }

    public void UploadImage() {
    }


}
