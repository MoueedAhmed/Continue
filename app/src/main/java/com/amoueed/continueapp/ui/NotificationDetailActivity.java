package com.amoueed.continueapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.amoueed.continueapp.R;

import org.apache.commons.io.FilenameUtils;

public class NotificationDetailActivity extends AppCompatActivity {

    private static final String RESOURCE_PATH = "resourcePath";

    private Intent intent;
    private String resourcePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        setTitle("CoNTiNuE");

        intent = getIntent();
        resourcePath = intent.getStringExtra(RESOURCE_PATH);

        Toast.makeText(NotificationDetailActivity.this, getExtension(resourcePath), Toast.LENGTH_LONG).show();
    }

    public String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }
}
