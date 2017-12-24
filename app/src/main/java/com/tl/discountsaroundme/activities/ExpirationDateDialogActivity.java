package com.tl.discountsaroundme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.DatePicker;

import com.tl.discountsaroundme.R;

public class ExpirationDateDialogActivity extends AppCompatActivity {

    Button confirm;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiration_date_dialog);

        confirm = findViewById(R.id.confirmExportButton);
        datePicker = findViewById(R.id.datePickerChoose);



    }
}
