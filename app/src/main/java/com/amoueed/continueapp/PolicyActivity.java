package com.amoueed.continueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;

public class PolicyActivity extends AppCompatActivity {

    private WebView policy_webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        setTitle("CoNTiNuE");
        this.setFinishOnTouchOutside(false);

        policy_webview = findViewById(R.id.policy_webview);
        policy_webview.getSettings().setJavaScriptEnabled(true);
        policy_webview.loadUrl("file:///android_asset/policy.html");
    }
}
