package com.amoueed.continueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amoueed.continueapp.main.MainActivity;
import com.amoueed.continueapp.splashscreen.SplashScreenActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;

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

        //[Start] Downloading content from Firebase
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String dirLocationFirebase = "initial_content/1";
        // Create a reference with an initial file path and name
        StorageReference dirReference = storageRef.child(dirLocationFirebase);

        //final File directory = getStorageDir(SuccessActivity.this, "content");

        dirReference.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            File file = null;
                            try {
                                file = new File(getFilesDir(), item.getName());
                            } catch (Exception e) {
                                Toast.makeText(SuccessActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            final ProgressDialog dialog = new ProgressDialog(SuccessActivity.this);
                            dialog.setMessage("Downloading content, please wait.");
                            dialog.show();

                            item.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    dialog.dismiss();
//                                    Toast.makeText(SuccessActivity.this, "Downloading: "
//                                            +taskSnapshot.getStorage().getName(), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    dialog.dismiss();
                                    Toast.makeText(SuccessActivity.this, "Failed downloading", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SuccessActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //[End] Downloading content from Firebase

    }

    //get Internal storage directory and create new directory having name dirName as argument
//    public File getStorageDir(Context context, String dirName) {
//        // Get the directory
//        File dir = new File(context.getFilesDir(), dirName);
//        if (!dir.mkdirs()) {
//            Toast.makeText(SuccessActivity.this,
//                    "Failed creating directory " + dirName, Toast.LENGTH_SHORT).show();
//        }
//        return dir;
//    }

    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
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

        mDatabase.child("enrollments").child(userId).setValue(childEnrollment);
    }
    // [END basic_write]

}
