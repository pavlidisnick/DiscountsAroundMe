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

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ImageView imageView = findViewById(R.id.back_button);
        imageView.setOnClickListener(this);

        Button submitButton = findViewById(R.id.submitFeedback);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submitFeedback)
            sendFeedback();
        else
            finish();
    }

    private void sendFeedback() {
        EditText editText = findViewById(R.id.feedbackContent);
        String feedbackContent = editText.getText().toString().replaceAll("\n", "");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("feedback");
        ref.push().setValue(feedbackContent);

        Toast.makeText(getApplicationContext(), "Feedback submitted", Toast.LENGTH_SHORT).show();
        finish();
    }
}
