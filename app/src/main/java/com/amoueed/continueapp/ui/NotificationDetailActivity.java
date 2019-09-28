package com.amoueed.continueapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import com.amoueed.continueapp.R;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class NotificationDetailActivity extends AppCompatActivity {

    private static final String FILE_NAME = "fileName";

    private Intent intent;
    private String fileName;
    private TextView info_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        fileName = intent.getStringExtra(FILE_NAME);

        setContentView(R.layout.activity_notification_detail);
        setTitle("CoNTiNuE");

        info_text = findViewById(R.id.info_text);

        if(getExtension(fileName).equals("txt")){
            File file = new File(getFilesDir(),fileName);
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();

                info_text.setText(text);
            }
            catch (IOException e) {
                Toast.makeText(NotificationDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }
}
