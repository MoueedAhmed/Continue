package com.amoueed.continueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.amoueed.continueapp.ui.SuccessActivity;
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


public class EnrollmentActivity extends AppCompatActivity{
    private Spinner mode_spinner;
    private List<String> languageArray;
    private ArrayAdapter<String> adapter;
    private Spinner language_spinner;
    private Button register_button;
    private ProgressDialog progressDialog;
    private Calendar myCalendar;
    private TextInputEditText child_dob_et;
    private TextInputEditText child_mr_et;
    private String childMR;
    private String childDOB;
    private String mode;
    private String language;
    private String barrier;
    private String preferredTime;
    private Spinner barrier_spinner;
    private Spinner preferred_time_spinner;
    private static final String TAG = "EnrollmentActivity";
    private static final String PASSWORD = "RaoMoueedAhmed1";
    private static final String CHILD_DOB = "childDOB";
    private static final String CHILD_MR = "childMR";
    private static final String MODE = "mode";
    private static final String LANGUAGE = "language";
    private static final String BARRIER = "barrier";
    private static final String PREFERRED_TIME = "preferredTime";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);
        setTitle("CoNTINuE");

        mode_spinner = findViewById(R.id.mode_spinner);
        language_spinner = findViewById(R.id.language_spinner);
        register_button = findViewById(R.id.register_button);
        progressDialog = new ProgressDialog(this);
        child_dob_et = findViewById(R.id.child_dob_et);
        child_mr_et = findViewById(R.id.child_mr_et);
        barrier_spinner = findViewById(R.id.barrier_spinner);
        preferred_time_spinner = findViewById(R.id.preferred_time_spinner);
        mAuth = FirebaseAuth.getInstance();

        //[enter]
        //dynamically set languages spinner using mode spinner values
        mode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // you need to have a list of data that you want the spinner to display
                languageArray = new ArrayList<String>();


                adapter = new ArrayAdapter<String>(
                        EnrollmentActivity.this, android.R.layout.simple_spinner_item, languageArray);

                if (mode_spinner.getSelectedItem().toString().equals("Text")) {
                    languageArray.add("Urdu");
                    languageArray.add("Sindhi");
                    languageArray.add("Urdu Roman");
                    languageArray.add("Sindhi Roman");
                    adapter.notifyDataSetChanged();
                } else {
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
                languageArray.add("Urdu");

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        EnrollmentActivity.this, android.R.layout.simple_spinner_item, languageArray);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                language_spinner = findViewById(R.id.language_spinner);
                language_spinner.setAdapter(adapter);
            }
        });
        //[end]
        //dynamically set languages spinner using mode spinner values

        //[enter]
        //create user and password only if data is valid
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isDataValid = validateData();

                if (isDataValid) {
                    progressDialog.setMessage("Processing...");
                    progressDialog.show();
                    //getting MR Number and create user by setting default password "RaoMoueedAhmed1"
                    createAccount(childMR+"@continue.com", PASSWORD);
                }
            }
        });
        //[end]
        //create user and password only if data is valid

        //[enter]
        //show error if MR number is less than 7
        child_mr_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (child_mr_et.getText().toString().trim().length() < 7) {
                    child_mr_et.setError("Enter 7 digits");
                } else {
                    child_mr_et.setError(null);
                }
            }
        });
        //[end]
        //show error if MR number is less than 7

        datePickerSetter();
    }

    //[enter]
    //create firebase credentials using MR Number and default password
    private void createAccount(String mr_no, String password) {

        mAuth.createUserWithEmailAndPassword(mr_no, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent in = new Intent(EnrollmentActivity.this, WelcomeActivity.class)
                                    .putExtra(CHILD_DOB, childDOB)
                                    .putExtra(CHILD_MR, childMR)
                                    .putExtra(MODE, mode)
                                    .putExtra(LANGUAGE, language)
                                    .putExtra(BARRIER, barrier)
                                    .putExtra(PREFERRED_TIME, preferredTime);
                            progressDialog.dismiss();
                            startActivity(in);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(EnrollmentActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    //[end]
    //create firebase credentials using MR Number and default password

    private boolean validateData() {
        //validation of data

        childMR = child_mr_et.getText().toString().trim();
        if (TextUtils.isEmpty(childMR)) {
            child_mr_et.setError("Required!");
            return false;
        }

        childDOB = child_dob_et.getText().toString().trim();
        if (TextUtils.isEmpty(childDOB)) {
            child_dob_et.setError("Required!");
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
        if (preferredTime.equals("Notification Time")) {
            Toast.makeText(EnrollmentActivity.this,
                    "Error: Notification Time", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void datePickerSetter() {

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        child_dob_et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(EnrollmentActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        child_dob_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
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

}