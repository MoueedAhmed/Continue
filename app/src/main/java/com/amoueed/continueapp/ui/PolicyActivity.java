package com.amoueed.continueapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.amoueed.continueapp.R;

public class PolicyActivity extends AppCompatActivity {

    private WebView policy_webview;
    private Button agree_btn;
    private Button disagree_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        setTitle("CoNTiNuE");
        this.setFinishOnTouchOutside(false);

        policy_webview = findViewById(R.id.policy_webview);
        policy_webview.getSettings().setJavaScriptEnabled(true);
        policy_webview.loadUrl("file:///android_asset/policy.html");

        agree_btn = findViewById(R.id.agree_btn);
        agree_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(PolicyActivity.this, EnrollmentActivity.class);
                startActivity(in);
                finish();
            }
        });

        disagree_btn = findViewById(R.id.disagree_btn);
        disagree_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
