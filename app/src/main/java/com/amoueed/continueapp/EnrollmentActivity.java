package com.amoueed.continueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amoueed.continueapp.main.MainActivity;
import com.amoueed.continueapp.splashscreen.SplashScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class EnrollmentActivity extends AppCompatActivity {

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
    private Spinner relation_spinner;
    private Spinner mode_spinner;
    private Spinner language_spinner;
    private Spinner barrier_spinner;
    private Spinner preferred_time_spinner;
    private CheckBox terms_checkBox;

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

    private Calendar myCalendar;
    private List<String> languageArray;
    private ArrayAdapter<String> adapter;

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
        relation_spinner = findViewById(R.id.relation_spinner);
        mode_spinner = findViewById(R.id.mode_spinner);
        language_spinner = findViewById(R.id.language_spinner);
        barrier_spinner = findViewById(R.id.barrier_spinner);
        preferred_time_spinner = findViewById(R.id.preferred_time_spinner);
        terms_checkBox = findViewById(R.id.terms_checkBox);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isDataValid = validateData();

                if (isDataValid) {
                    if (terms_checkBox.isChecked()) {
                        progressDialog.setMessage("Processing...");
                        progressDialog.show();
                        //getting mobile number and create user by setting default password "RaoMoueedAhmed1"
                        createAccount(contactNo + "@continue.com", PASSWORD);
                    } else {
                        Toast.makeText(EnrollmentActivity.this,
                                "Accept terms and Conditions to continue registration",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // you need to have a list of data that you want the spinner to display
                languageArray = new ArrayList<String>();


                adapter = new ArrayAdapter<String>(
                        EnrollmentActivity.this, android.R.layout.simple_spinner_item, languageArray);

                if (mode_spinner.getSelectedItem().toString().equals("Text One Way")
                        || mode_spinner.getSelectedItem().toString().equals("Text Two Way")) {
                    languageArray.add("Language");
                    languageArray.add("English");
                    languageArray.add("Urdu");
                    languageArray.add("Sindhi");
                    languageArray.add("Urdu Roman");
                    languageArray.add("Sindhi Roman");
                    adapter.notifyDataSetChanged();
                } else {
                    languageArray.add("Language");
                    languageArray.add("English");
                    languageArray.add("Urdu");
                    languageArray.add("Sindhi");
                    adapter.notifyDataSetChanged();
                }

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                language_spinner = findViewById(R.id.language_spinner);
                language_spinner.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // you need to have a list of data that you want the spinner to display
                List<String> languageArray = new ArrayList<String>();
                languageArray.add("Language");

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        EnrollmentActivity.this, android.R.layout.simple_spinner_item, languageArray);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                language_spinner = findViewById(R.id.language_spinner);
                language_spinner.setAdapter(adapter);
            }
        });

        datePickerSetter();
    }

    private void datePickerSetter() {

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        child_dob_et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EnrollmentActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        child_dob_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(EnrollmentActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        child_dob_et.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean validateData() {
        //validation of data
        childName = child_name_et.getText().toString().trim();
        if (TextUtils.isEmpty(childName)) {
            child_name_et.setError("Required!");
            return false;
        }

        childDOB = child_dob_et.getText().toString().trim();
        if (TextUtils.isEmpty(childDOB)) {
            child_dob_et.setError("Required!");
            return false;
        }

        childGender = gender_spinner.getSelectedItem().toString();
        if (childGender.equals("Gender")) {
            Toast.makeText(EnrollmentActivity.this, "Error: Select Gender", Toast.LENGTH_LONG).show();
            return false;
        }

        childMR = child_mr_et.getText().toString().trim();
        if (TextUtils.isEmpty(childMR)) {
            child_mr_et.setError("Required!");
            return false;
        }

        contactNo = contact_et.getText().toString().trim();
        if (TextUtils.isEmpty(contactNo)) {
            contact_et.setError("Required!");
            return false;
        }

        childRelative = relation_spinner.getSelectedItem().toString();
        if (childRelative.equals("Relation")) {
            Toast.makeText(EnrollmentActivity.this, "Error: Select Relation", Toast.LENGTH_LONG).show();
            return false;
        }

        mode = mode_spinner.getSelectedItem().toString();
        if (mode.equals("Mode")) {
            Toast.makeText(EnrollmentActivity.this, "Error: Select Mode", Toast.LENGTH_LONG).show();
            return false;
        }

        language = language_spinner.getSelectedItem().toString();
        if (language.equals("Language")) {
            Toast.makeText(EnrollmentActivity.this, "Error: Select language", Toast.LENGTH_LONG).show();
            return false;
        }

        barrier = barrier_spinner.getSelectedItem().toString();
        if (barrier.equals("Barrier")) {
            Toast.makeText(EnrollmentActivity.this, "Error: Select barrier", Toast.LENGTH_LONG).show();
            return false;
        }

        preferredTime = preferred_time_spinner.getSelectedItem().toString();
        if (preferredTime.equals("Preferred Notification Time")) {
            Toast.makeText(EnrollmentActivity.this,
                    "Error: Select Preferred Time for Notifications", Toast.LENGTH_LONG).show();
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
                                    .putExtra(CHILD_NAME, childName)
                                    .putExtra(CHILD_DOB, childDOB)
                                    .putExtra(CHILD_GENDER, childGender)
                                    .putExtra(CHILD_MR, childMR)
                                    .putExtra(CONTACT_NO, contactNo)
                                    .putExtra(CHILD_RELATIVE, childRelative)
                                    .putExtra(MODE, mode)
                                    .putExtra(LANGUAGE, language)
                                    .putExtra(BARRIER, barrier)
                                    .putExtra(PREFERRED_TIME, preferredTime);
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
