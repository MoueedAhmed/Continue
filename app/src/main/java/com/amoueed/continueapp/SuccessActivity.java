package com.amoueed.continueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

        Toast.makeText(SuccessActivity.this, childName+" "+barrier, Toast.LENGTH_LONG).show();
    }
}
