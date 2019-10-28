package com.amoueed.continueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.amoueed.continueapp.firebasemodel.EnrollmentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeActivity extends AppCompatActivity {

    private static final String CHILD_MR = "childMR";
    private static final String CHILD_DOB = "childDOB";
    private static final String MODE = "mode";
    private static final String LANGUAGE = "language";
    private static final String BARRIER = "barrier";
    private static final String PREFERRED_TIME = "preferredTime";
    private Intent intent;
    private String childMR;
    private String childDOB;
    private String mode;
    private String language;
    private String barrier;
    private String preferredTime;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        intent = getIntent();
        childMR = intent.getStringExtra(CHILD_MR);
        childDOB = intent.getStringExtra(CHILD_DOB);
        mode = intent.getStringExtra(MODE);
        language = intent.getStringExtra(LANGUAGE);
        barrier = intent.getStringExtra(BARRIER);
        preferredTime = intent.getStringExtra(PREFERRED_TIME);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        insertEnrollmentToFirebase();

    }

    //[start]
    //Insert enrollment data to firebase
    private void insertEnrollmentToFirebase() {
        EnrollmentModel enrollmentModel = new EnrollmentModel(childMR, childDOB, mode, language, barrier, preferredTime);
        mDatabase.child("app_data").child(childMR).setValue(enrollmentModel);
    }
    //[end]
    //Insert enrollment data to firebase
}

