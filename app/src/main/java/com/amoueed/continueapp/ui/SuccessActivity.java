package com.amoueed.continueapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amoueed.continueapp.worker.DailyWorker;
import com.amoueed.continueapp.R;
import com.amoueed.continueapp.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

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
    private Button continue_success_btn;
    private Button exit_success_btn;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    // Notification ID.
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotificationManager;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

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

        continue_success_btn = findViewById(R.id.continue_success_btn);
        exit_success_btn = findViewById(R.id.exit_success_btn);

        continue_success_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(SuccessActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        });

        exit_success_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        child_name_success_tv = findViewById(R.id.child_name_success_tv);
        child_name_success_tv.setText("Name: " + childName);

        child_dob_success_tv = findViewById(R.id.child_dob_success_tv);
        child_dob_success_tv.setText("DOB: " + childDOB);

        child_gender_success_tv = findViewById(R.id.child_gender_success_tv);
        child_gender_success_tv.setText("Gender: " + childGender);

        child_mr_success_tv = findViewById(R.id.child_mr_success_tv);
        child_mr_success_tv.setText("MR Number: " + childMR);

        contact_success_tv = findViewById(R.id.contact_success_tv);
        contact_success_tv.setText("Contact Number: " + contactNo);

        relative_success_tv = findViewById(R.id.relative_success_tv);
        relative_success_tv.setText("Relation with Child: " + childRelative);

        mode_success_tv = findViewById(R.id.mode_success_tv);
        mode_success_tv.setText("Mode: " + mode);

        language_success_tv = findViewById(R.id.language_success_tv);
        language_success_tv.setText("Language: " + language);

        barrier_success_tv = findViewById(R.id.barrier_success_tv);
        barrier_success_tv.setText("Barrier: " + barrier);

        preferred_time_success_tv = findViewById(R.id.preferred_time_success_tv);
        preferred_time_success_tv.setText("Preferred Time of Notifications: " + preferredTime);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        createChannel();
//
//        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder
//                (WeekWorker.class, 15, TimeUnit.MINUTES)
//                .setInitialDelay(2,TimeUnit.MINUTES)
//                .build();
//        WorkManager.getInstance(SuccessActivity.this).enqueue(workRequest);
//
//        PeriodicWorkRequest workRequest2 = new PeriodicWorkRequest.Builder
//                (DailyWorker.class, 15, TimeUnit.MINUTES)
//                .build();
//        WorkManager.getInstance(SuccessActivity.this).enqueue(workRequest2);

    }

    private void createChannel(){
        // Create a notification manager object.
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID, "Stand up notification", NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies every 15 minutes to stand up and walk");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }



    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity enter
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        // Write new user
        writeNewChildEnrollment(user.getUid(), username, user.getEmail());
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    // [START basic_write]
    private void writeNewChildEnrollment(String userId, String name, String email) {
        ChildEnrollment childEnrollment = new ChildEnrollment(childName, childDOB, childGender, childMR, contactNo, childRelative,
                mode, language, barrier, preferredTime);

        mDatabase.child(userId).child("enrollment").setValue(childEnrollment);
    }
    // [END basic_write]
}
