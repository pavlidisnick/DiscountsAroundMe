package com.tl.discountsaroundme.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.tl.discountsaroundme.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ExpirationDateDialogActivity extends AppCompatActivity{
    Calendar calendar = Calendar.getInstance();
    String selectDate;
    Button confirm;
    Date yearMonthDate;
    DatePicker datePicker;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiration_date_dialog);

        confirm = findViewById(R.id.confirmExportButton);
        datePicker = findViewById(R.id.datePickerChoose);

        calendar.setTimeInMillis(System.currentTimeMillis());

        long now = System.currentTimeMillis() - 1000;
        datePicker.setMinDate(now);

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int date) {
                selectDate = date + "/" + (month+1) + "/" + year;
                Log.d("ACTIVITY DATEPICKER: ","Change DATE Convert to String :  " + selectDate);
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    yearMonthDate = dateFormat.parse(selectDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("ACTIVITY DATEPICKER: ","Change DATE  :  " + yearMonthDate);

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(yearMonthDate!=null) {
                    Intent intent = new Intent();
                    intent.putExtra("date", yearMonthDate);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"Choose the date of export your item",Toast.LENGTH_LONG).show();
            }
        });


    }

}
