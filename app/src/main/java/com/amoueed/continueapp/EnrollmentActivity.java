package com.amoueed.continueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class EnrollmentActivity extends AppCompatActivity{

    private static final String TAG = "EnrollmentActivity";
    private static final String PASSWORD = "RaoMoueedAhmed1";

    private TextInputEditText contact_et;
    private Button register_button;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        contact_et = findViewById(R.id.contact_et);
        register_button = findViewById(R.id.register_button);
        mAuth = FirebaseAuth.getInstance();


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting mobile number and create user by setting default password "RaoMoueedAhmed1"
                createAccount(contact_et.getText().toString()+"@continue.com",PASSWORD);
            }
        });
    }


    private void createAccount(String email, String password) {

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(EnrollmentActivity.this, user.getUid(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EnrollmentActivity.this, task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }

}
