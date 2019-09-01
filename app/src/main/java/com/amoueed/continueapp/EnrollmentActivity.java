package com.amoueed.continueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.amoueed.continueapp.main.MainActivity;
import com.amoueed.continueapp.splashscreen.SplashScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class EnrollmentActivity extends AppCompatActivity{

    private static final String TAG = "EnrollmentActivity";
    private static final String PASSWORD = "RaoMoueedAhmed1";
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

    private Button register_button;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private TextInputEditText child_name_et;
    private TextInputEditText child_dob_et;
    private Spinner gender_spinner;
    private TextInputEditText child_mr_et;
    private TextInputEditText contact_et;
    private TextInputEditText child_relation_et;
    private Spinner mode_spinner;
    private Spinner language_spinner;
    private Spinner barrier_spinner;
    private TextInputEditText preferred_time_et;

    private String contactNo;
    private String childName;
    private String childDOB;
    private String childGender;
    private String childMR;
    private String childRelative;
    private String mode;
    private String language;
    private String barrier;
    private String preferredTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        contact_et = findViewById(R.id.contact_et);
        register_button = findViewById(R.id.register_button);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        child_name_et = findViewById(R.id.child_name_et);
        child_dob_et = findViewById(R.id.child_dob_et);
        gender_spinner = findViewById(R.id.gender_spinner);
        child_mr_et = findViewById(R.id.child_mr_et);
        child_relation_et = findViewById(R.id.child_relation_et);
        mode_spinner = findViewById(R.id.mode_spinner);
        language_spinner = findViewById(R.id.language_spinner);
        barrier_spinner = findViewById(R.id.barrier_spinner);
        preferred_time_et = findViewById(R.id.preferred_time_et);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isDataValid = validateData();

                if (isDataValid){
                    progressDialog.setMessage("Processing...");
                    progressDialog.show();
                    //getting mobile number and create user by setting default password "RaoMoueedAhmed1"
                    createAccount(contactNo+"@continue.com",PASSWORD);
                }
            }
        });
    }

    private boolean validateData() {
        //validation of data
        childName = child_name_et.getText().toString().trim();
        if(TextUtils.isEmpty(childName)){
            child_name_et.setError("Required!");
            return false;
        }

        childDOB = child_dob_et.getText().toString().trim();
        if(TextUtils.isEmpty(childDOB)){
            child_dob_et.setError("Required!");
            return false;
        }

        childGender = gender_spinner.getSelectedItem().toString();

        childMR = child_mr_et.getText().toString().trim();
        if(TextUtils.isEmpty(childMR)){
            child_mr_et.setError("Required!");
            return false;
        }

        contactNo = contact_et.getText().toString().trim();
        if(TextUtils.isEmpty(contactNo)){
            contact_et.setError("Required!");
            return false;
        }

        childRelative = child_relation_et.getText().toString().trim();
        if(TextUtils.isEmpty(childRelative)){
            child_relation_et.setError("Required!");
            return false;
        }

        mode = mode_spinner.getSelectedItem().toString();

        language = language_spinner.getSelectedItem().toString();

        barrier = barrier_spinner.getSelectedItem().toString();

        preferredTime = preferred_time_et.getText().toString().trim();
        if(TextUtils.isEmpty(preferredTime)){
            preferred_time_et.setError("Required!");
            return false;
        }
        return true;
    }


    private void createAccount(String email, String password) {

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent in = new Intent(EnrollmentActivity.this, SuccessActivity.class)
                                    .putExtra(CHILD_NAME,childName)
                                    .putExtra(CHILD_DOB,childDOB)
                                    .putExtra(CHILD_GENDER,childGender)
                                    .putExtra(CHILD_MR,childMR)
                                    .putExtra(CONTACT_NO,contactNo)
                                    .putExtra(CHILD_RELATIVE,childRelative)
                                    .putExtra(MODE,mode)
                                    .putExtra(LANGUAGE,language)
                                    .putExtra(BARRIER,barrier)
                                    .putExtra(PREFERRED_TIME,preferredTime);
                            progressDialog.dismiss();
                            startActivity(in);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(EnrollmentActivity.this, task.getException().toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }

}
