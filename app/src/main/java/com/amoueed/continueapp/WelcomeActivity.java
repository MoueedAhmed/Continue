package com.amoueed.continueapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amoueed.continueapp.firebasemodel.EnrollmentModel;
import com.amoueed.continueapp.main.MainActivity;
import com.amoueed.continueapp.ui.SuccessActivity;
import com.amoueed.continueapp.worker.WeeklyNotificationWorker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

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
    private Dictionary<String, String> welcomeMessages;
    private Button start_btn;
    private Button exit_btn;
    private String contentType;
    private TextView welcome_tv;
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotificationManager;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getDataFromIntent();
        contentType = checkCombinationAndSetContentIdentifierSharedPref(mode, language, barrier);
        syncWithFirebaseDatabase();
        createChannel();

        setWeeklyNotificationWorker();

        start_btn = findViewById(R.id.start_btn);
        exit_btn = findViewById(R.id.exit_btn);
        welcome_tv = findViewById(R.id.welcome_tv);

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EnrollmentModel enrollmentModel = new EnrollmentModel(childMR, childDOB, mode, language, barrier, preferredTime);
//                mDatabase.child("app_data").child("test").setValue(enrollmentModel);
                Intent in = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        });

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EnrollmentModel enrollmentModel = new EnrollmentModel(childMR, childDOB, mode, language, barrier, preferredTime);
//                mDatabase.child("app_data").child("newTest").child("test").setValue(enrollmentModel);
                finish();
            }
        });

        addWelcomeMessages();

        insertEnrollmentToFirebase();


        setWelcomeTextViewMessage(welcome_tv, contentType);
        downloadContent(contentType);
    }

    private void setWeeklyNotificationWorker() {

        String dobString = childDOB;
        Date dob = null;
        try {
            dob=new SimpleDateFormat("dd/MM/yy").parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date dateCurrent = new Date();

        long diff = dateCurrent.getTime() - dob.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        int initialDelayinDays = 42- (int) days;

        String preferredNotificationTime = preferredTime;

        int initialDelay=1;
        TimeUnit delayUnit=TimeUnit.MINUTES;

        if(initialDelayinDays>0){
            //if enrollment is in Morning
            if (dateCurrent.getHours() >= 6 && dateCurrent.getHours() <= 11
                    && preferredNotificationTime.equals("Any Time")) {

                initialDelay = initialDelayinDays*24;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                        "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }
            else if (dateCurrent.getHours() >= 6 && dateCurrent.getHours() <= 11
                    && preferredNotificationTime.equals("Morning")) {

                initialDelay = initialDelayinDays*24;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }

            else if (dateCurrent.getHours() >= 6 && dateCurrent.getHours() <= 11
                    && preferredNotificationTime.equals("Afternoon")) {

                initialDelay = (initialDelayinDays*24)+4;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }
            else if (dateCurrent.getHours() >= 6 && dateCurrent.getHours() <= 11
                    && preferredNotificationTime.equals("Evening")) {

                initialDelay = (initialDelayinDays*24)+10;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }
            //if enrollment is in Afternoon
            else if (dateCurrent.getHours() >= 12 && dateCurrent.getHours() <= 16
                    && preferredNotificationTime.equals("Any Time")) {

                initialDelay = initialDelayinDays*24;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }
            else if (dateCurrent.getHours() >= 12 && dateCurrent.getHours() <= 16
                    && preferredNotificationTime.equals("Morning")) {

                initialDelay = (initialDelayinDays*24) + 20;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }

            else if (dateCurrent.getHours() >= 12 && dateCurrent.getHours() <= 16
                    && preferredNotificationTime.equals("Afternoon")) {

                initialDelay = initialDelayinDays*24;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }
            else if (dateCurrent.getHours() >= 12 && dateCurrent.getHours() <= 16
                    && preferredNotificationTime.equals("Evening")) {

                initialDelay = (initialDelayinDays*24)+4;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }
            //if enrollment is in Evening
            else if (dateCurrent.getHours() >= 17 && dateCurrent.getHours() <= 23
                    && preferredNotificationTime.equals("Any Time")) {

                initialDelay = initialDelayinDays*24;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }
            else if (dateCurrent.getHours() >= 17 && dateCurrent.getHours() <= 23
                    && preferredNotificationTime.equals("Morning")) {

                initialDelay = (initialDelayinDays*24) + 16;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }

            else if (dateCurrent.getHours() >= 12 && dateCurrent.getHours() <= 16
                    && preferredNotificationTime.equals("Afternoon")) {

                initialDelay = (initialDelayinDays*24)+20;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }
            else if (dateCurrent.getHours() >= 12 && dateCurrent.getHours() <= 16
                    && preferredNotificationTime.equals("Evening")) {

                initialDelay = initialDelayinDays*24;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }
            //if enrollment is in Night
            else if (dateCurrent.getHours() >= 0 && dateCurrent.getHours() <= 5
                    && preferredNotificationTime.equals("Any Time")) {

                initialDelay = initialDelayinDays*24;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }
            else if (dateCurrent.getHours() >= 0 && dateCurrent.getHours() <= 5
                    && preferredNotificationTime.equals("Morning")) {

                initialDelay = (initialDelayinDays*24) + 12;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }

            else if (dateCurrent.getHours() >= 0 && dateCurrent.getHours() <= 5
                    && preferredNotificationTime.equals("Afternoon")) {

                initialDelay = (initialDelayinDays*24)+16;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }
            else if (dateCurrent.getHours() >= 0 && dateCurrent.getHours() <= 5
                    && preferredNotificationTime.equals("Evening")) {

                initialDelay = initialDelayinDays*24;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }
            else if (dateCurrent.getHours() >= 0 && dateCurrent.getHours() <= 5
                    && preferredNotificationTime.equals("Evening")) {

                initialDelay = initialDelayinDays * 24;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: " + preferredNotificationTime + "\n" +
                                "Hours left: " + initialDelay,
                        Toast.LENGTH_LONG).show();
            }
            else {

                initialDelay = initialDelayinDays*24;
                delayUnit = TimeUnit.HOURS;

                Toast.makeText(WelcomeActivity.this,
                        "Notification Time: "+ preferredNotificationTime+"\n"+
                                "Hours left: "+initialDelay,
                        Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(WelcomeActivity.this,
                    "Child age is bigger than required age for this application. App will not work properly",
                    Toast.LENGTH_LONG).show();
        }

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest
                .Builder(WeeklyNotificationWorker.class, 15, TimeUnit.MINUTES)
                .setInitialDelay(initialDelay,delayUnit)
                .build();
        WorkManager.getInstance(WelcomeActivity.this).enqueue(workRequest);
    }

    private void getDataFromIntent() {
        intent = getIntent();
        childMR = intent.getStringExtra(CHILD_MR);
        childDOB = intent.getStringExtra(CHILD_DOB);
        mode = intent.getStringExtra(MODE);
        language = intent.getStringExtra(LANGUAGE);
        barrier = intent.getStringExtra(BARRIER);
        preferredTime = intent.getStringExtra(PREFERRED_TIME);
    }

    private void syncWithFirebaseDatabase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
    }

    private void addWelcomeMessages() {
        welcomeMessages = new Hashtable<>();
        welcomeMessages.put("u", "پروجیکٹ کنٹینیو کی طرف سے ہر ہفتے آپ کو پیغام ملتا رہے گا" +
                " بچوں کو تمام حفاظتی ٹیکے وقت پر لگوائیں");
        welcomeMessages.put("ru", "Project CoNTINuE  ki taraf se Har hafte ap ko paigham milta rhega." +
                " Bacho  ko tamam Hifazati tekay waqt per lagwaye");
        welcomeMessages.put("s", "پروجیکٹ کنٹینیو  جي طرف کان هر هفتي توهان کي " +
                " پيغام ملندو رهندو، ٻارن جي لاءِ ضروري آهي ته تمام حفاظتي ٽڪا وقت تي لڳرايا وڃن");
        welcomeMessages.put("rs", "Project CoNTINuE  jay taraf kan har haftay tawan kay pegham milando" +
                " rehando, baran kay sabhi hifazti teeka wakt tay lagayo.");
    }

    //[enter]
    //Insert enrollment data to firebase
    private void insertEnrollmentToFirebase() {
        EnrollmentModel enrollmentModel = new EnrollmentModel(childMR, childDOB, mode, language, barrier, preferredTime);
        mDatabase.child("app_data").child(childMR).setValue(enrollmentModel);
    }
    //[end]
    //Insert enrollment data to firebase

    private String checkCombinationAndSetContentIdentifierSharedPref(String mode, String language, String barrier) {

        String content_identifier_value = null;

        if (mode.equals("Text") && language.equals("Urdu") && barrier.equals("Reminder")) {
            content_identifier_value = "t_u_reminder";
        } else if (mode.equals("Text") && language.equals("Urdu") && barrier.equals("Educational")) {
            content_identifier_value = "t_u_educational";
        } else if (mode.equals("Text") && language.equals("Urdu") && barrier.equals("Adverse effect")) {
            content_identifier_value = "t_u_adverse";
        } else if (mode.equals("Text") && language.equals("Urdu") && barrier.equals("Religious")) {
            content_identifier_value = "t_u_religious";
        } else if (mode.equals("Text") && language.equals("Urdu") && barrier.equals("Combo")) {
            content_identifier_value = "t_u_combo";
        } else if (mode.equals("Text") && language.equals("Urdu Roman") && barrier.equals("Reminder")) {
            content_identifier_value = "t_ru_reminder";
        } else if (mode.equals("Text") && language.equals("Urdu Roman") && barrier.equals("Educational")) {
            content_identifier_value = "t_ru_educational";
        } else if (mode.equals("Text") && language.equals("Urdu Roman") && barrier.equals("Adverse effect")) {
            content_identifier_value = "t_ru_adverse";
        } else if (mode.equals("Text") && language.equals("Urdu Roman") && barrier.equals("Religious")) {
            content_identifier_value = "t_ru_religious";
        } else if (mode.equals("Text") && language.equals("Urdu Roman") && barrier.equals("Combo")) {
            content_identifier_value = "t_ru_combo";
        } else if (mode.equals("Text") && language.equals("Sindhi") && barrier.equals("Reminder")) {
            content_identifier_value = "t_s_reminder";
        } else if (mode.equals("Text") && language.equals("Sindhi") && barrier.equals("Educational")) {
            content_identifier_value = "t_s_educational";
        } else if (mode.equals("Text") && language.equals("Sindhi") && barrier.equals("Adverse effect")) {
            content_identifier_value = "t_s_adverse";
        } else if (mode.equals("Text") && language.equals("Sindhi") && barrier.equals("Religious")) {
            content_identifier_value = "t_s_religious";
        } else if (mode.equals("Text") && language.equals("Sindhi") && barrier.equals("Combo")) {
            content_identifier_value = "t_s_combo";
        } else if (mode.equals("Text") && language.equals("Sindhi Roman") && barrier.equals("Reminder")) {
            content_identifier_value = "t_rs_reminder";
        } else if (mode.equals("Text") && language.equals("Sindhi Roman") && barrier.equals("Educational")) {
            content_identifier_value = "t_rs_educational";
        } else if (mode.equals("Text") && language.equals("Sindhi Roman") && barrier.equals("Adverse effect")) {
            content_identifier_value = "t_rs_adverse";
        } else if (mode.equals("Text") && language.equals("Sindhi Roman") && barrier.equals("Religious")) {
            content_identifier_value = "t_rs_religious";
        } else if (mode.equals("Text") && language.equals("Sindhi Roman") && barrier.equals("Combo")) {
            content_identifier_value = "t_rs_combo";
        } else if (mode.equals("Audio") && language.equals("Urdu") && barrier.equals("Reminder")) {
            content_identifier_value = "a_u_reminder";
        } else if (mode.equals("Audio") && language.equals("Urdu") && barrier.equals("Educational")) {
            content_identifier_value = "a_u_educational";
        } else if (mode.equals("Audio") && language.equals("Urdu") && barrier.equals("Adverse effect")) {
            content_identifier_value = "a_u_adverse";
        } else if (mode.equals("Audio") && language.equals("Urdu") && barrier.equals("Religious")) {
            content_identifier_value = "a_u_religious";
        } else if (mode.equals("Audio") && language.equals("Urdu") && barrier.equals("Combo")) {
            content_identifier_value = "a_u_combo";
        } else if (mode.equals("Audio") && language.equals("Sindhi") && barrier.equals("Reminder")) {
            content_identifier_value = "a_s_reminder";
        } else if (mode.equals("Audio") && language.equals("Sindhi") && barrier.equals("Educational")) {
            content_identifier_value = "a_s_educational";
        } else if (mode.equals("Audio") && language.equals("Sindhi") && barrier.equals("Adverse effect")) {
            content_identifier_value = "a_s_educational";
        } else if (mode.equals("Audio") && language.equals("Sindhi") && barrier.equals("Religious")) {
            content_identifier_value = "a_s_religious";
        } else if (mode.equals("Audio") && language.equals("Sindhi") && barrier.equals("Combo")) {
            content_identifier_value = "a_s_combo";
        }

        SharedPreferences sharedPref = getSharedPreferences("content_identifier", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("content_identifier", content_identifier_value);
        editor.putString("mr_number", childMR);
        editor.putString("dob", childDOB);
        editor.commit();

        return content_identifier_value;
    }

    private void setWelcomeTextViewMessage(TextView welcome_tv, String contentType) {
        if (contentType.equals("t_u_reminder") || contentType.equals("t_u_educational")
                || contentType.equals("t_u_adverse") || contentType.equals("t_u_religious")
                || contentType.equals("t_u_combo") || contentType.equals("a_u_reminder")
                || contentType.equals("a_u_educational") || contentType.equals("a_u_adverse")
                || contentType.equals("a_u_religious") || contentType.equals("a_u_combo")) {

            welcome_tv.setText(welcomeMessages.get("u"));

        } else if (contentType.equals("t_s_reminder") || contentType.equals("t_s_educational")
                || contentType.equals("t_s_adverse") || contentType.equals("t_s_religious")
                || contentType.equals("t_s_combo") || contentType.equals("a_s_reminder")
                || contentType.equals("a_s_educational") || contentType.equals("a_s_adverse")
                || contentType.equals("a_s_religious") || contentType.equals("a_s_combo")) {

            welcome_tv.setText(welcomeMessages.get("s"));

        } else if (contentType.equals("t_ru_reminder") || contentType.equals("t_ru_educational")
                || contentType.equals("t_ru_adverse") || contentType.equals("t_ru_religious")
                || contentType.equals("t_ru_combo")) {

            welcome_tv.setText(welcomeMessages.get("ru"));

        } else if (contentType.equals("t_rs_reminder") || contentType.equals("t_rs_educational")
                || contentType.equals("t_rs_adverse") || contentType.equals("t_rs_religious")
                || contentType.equals("t_rs_combo")) {

            welcome_tv.setText(welcomeMessages.get("rs"));

        }
    }

    private void downloadContent(String contentIdentifier) {
        //[Start] Downloading content from Firebase
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String dirLocationFirebase = "content/" + contentIdentifier;
        // Create a reference with an initial file path and name
        StorageReference dirReference = storageRef.child(dirLocationFirebase);

        dirReference.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            File file = null;
                            try {
                                file = new File(getFilesDir(), item.getName());
                                Toast.makeText(WelcomeActivity.this, "Please wait, content is downloading!", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(WelcomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }


                            item.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception exception) {
                                    Toast.makeText(WelcomeActivity.this, "Failed downloading", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(WelcomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //[End] Downloading content from Firebase
    }

    private void createChannel() {
        // Create a notification manager object.
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID, "Notification from Continue", NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies every 15 week");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }
}

