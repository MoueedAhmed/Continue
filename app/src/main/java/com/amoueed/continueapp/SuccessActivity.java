package com.amoueed.continueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SuccessActivity extends AppCompatActivity {

    private static final String CHILD_NAME = "childName";
    private static final String CHILD_DOB = "childDOB";
    private static final String CHILD_GENDER = "childGender";
    private static final String CHILD_MR = "childMR";
    private static final String CONTACT_NO = "contactNo";
    private static final String CHILD_RELATIVE = "childRelative";
    private static final String MODE = "mode";
    private static final String LANGUAGE = "language";
    private static final String BARRIER = "barrier";
    private static final String PREFERRED_TIME = "preferredTime";

    private Intent intent;

    private String childName;
    private String childDOB;
    private String childGender;
    private String childMR;
    private String contactNo;
    private String childRelative;
    private String mode;
    private String language;
    private String barrier;
    private String preferredTime;

    private TextView child_name_success_tv;
    private TextView child_dob_success_tv;
    private TextView child_gender_success_tv;
    private TextView child_mr_success_tv;
    private TextView contact_success_tv;
    private TextView relative_success_tv;
    private TextView mode_success_tv;
    private TextView language_success_tv;
    private TextView barrier_success_tv;
    private TextView preferred_time_success_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        intent = getIntent();

        childName = intent.getStringExtra(CHILD_NAME);
        childDOB = intent.getStringExtra(CHILD_DOB);
        childGender = intent.getStringExtra(CHILD_GENDER);
        childMR = intent.getStringExtra(CHILD_MR);
        contactNo = intent.getStringExtra(CONTACT_NO);
        childRelative = intent.getStringExtra(CHILD_RELATIVE);
        mode = intent.getStringExtra(MODE);
        language = intent.getStringExtra(LANGUAGE);
        barrier = intent.getStringExtra(BARRIER);
        preferredTime = intent.getStringExtra(PREFERRED_TIME);

        child_name_success_tv = findViewById(R.id.child_name_success_tv);
        child_name_success_tv.setText("Name: "+childName);

        child_dob_success_tv = findViewById(R.id.child_dob_success_tv);
        child_dob_success_tv.setText("DOB: "+childDOB);

        child_gender_success_tv = findViewById(R.id.child_gender_success_tv);
        child_gender_success_tv.setText("Gender: "+childGender);

        child_mr_success_tv = findViewById(R.id.child_mr_success_tv);
        child_mr_success_tv.setText("MR Number: "+childMR);

        contact_success_tv = findViewById(R.id.contact_success_tv);
        contact_success_tv.setText("Contact Number: "+contactNo);

        relative_success_tv = findViewById(R.id.relative_success_tv);
        relative_success_tv.setText("Relation with Child: "+childRelative);

        mode_success_tv = findViewById(R.id.mode_success_tv);
        mode_success_tv.setText("Mode: "+mode);

        language_success_tv = findViewById(R.id.language_success_tv);
        language_success_tv.setText("Language: "+language);

        barrier_success_tv = findViewById(R.id.barrier_success_tv);
        barrier_success_tv.setText("Barrier: "+barrier);

        preferred_time_success_tv = findViewById(R.id.preferred_time_success_tv);
        preferred_time_success_tv.setText("Preferred Time of Notifications: "+preferredTime);
    }
}
