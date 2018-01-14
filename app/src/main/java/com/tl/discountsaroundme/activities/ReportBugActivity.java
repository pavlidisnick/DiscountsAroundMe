package com.tl.discountsaroundme.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tl.discountsaroundme.R;

public class ReportBugActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_bug);

        ImageView imageView = findViewById(R.id.back_button);
        imageView.setOnClickListener(this);

        Button submitButton = findViewById(R.id.reportBugButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reportBugButton)
            reportBug();
        else
            finish();
    }

    private void reportBug() {
        EditText editText = findViewById(R.id.bugDescription);
        String bugDescription = editText.getText().toString().replaceAll("\n","");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("bugs");
        ref.push().setValue(bugDescription);

        Toast.makeText(getApplicationContext(), "Bug submitted", Toast.LENGTH_SHORT).show();
        finish();
    }
}
