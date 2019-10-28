package com.amoueed.continueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amoueed.continueapp.firebasemodel.EnrollmentModel;
import com.amoueed.continueapp.main.MainActivity;
import com.amoueed.continueapp.ui.SuccessActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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
    private ArrayList<String> welcomeMessages;
    private Button start_btn;
    private Button exit_btn;
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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        start_btn = findViewById(R.id.start_btn);
        exit_btn = findViewById(R.id.exit_btn);

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        });

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        welcomeMessages = new ArrayList<>();
        welcomeMessages.add(0,"Aga Khan University k program Paigham e Sehat k taraf se Har hafte apko ek pegham milta rhega bacho  k lye zaruri hai k tamam Hifazati tekay wakt per lagwae jae ");

        insertEnrollmentToFirebase();

        TextView welcome_tv = findViewById(R.id.welcome_tv);

        if(new ContentIdentifier().CONTENT_IDENTIFIER.equals("0")){
            welcome_tv.setText(welcomeMessages.get(0));
        }

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

