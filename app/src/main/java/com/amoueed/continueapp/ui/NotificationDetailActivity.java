package com.amoueed.continueapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amoueed.continueapp.R;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NotificationDetailActivity extends AppCompatActivity {

    private static final String FILE_NAME = "fileName";

    private Intent intent;
    private String fileName;
    private TextView info_text;
    private LinearLayout audio_control_ll;

    private Button forward_button, pause_button, play_button, backward_button;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView counter_tv, length_tv;
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            seekbar.setProgress(0);
            startTime = mediaPlayer.getCurrentPosition();
            counter_tv.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        fileName = intent.getStringExtra(FILE_NAME);

        setContentView(R.layout.activity_notification_detail);
        setTitle("CoNTiNuE");

        info_text = findViewById(R.id.info_text);
        audio_control_ll = findViewById(R.id.audio_control_ll);


        if (getExtension(fileName).equals("txt")) {
            info_text.setVisibility(View.VISIBLE);
            audio_control_ll.setVisibility(View.GONE);
            File file = new File(getFilesDir(), fileName);
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
            } catch (IOException e) {
                Toast.makeText(NotificationDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (getExtension(fileName).equals("wav")) {
            info_text.setVisibility(View.GONE);
            audio_control_ll.setVisibility(View.VISIBLE);

            forward_button = findViewById(R.id.forward_button);
            pause_button = findViewById(R.id.pause_button);
            play_button = findViewById(R.id.play_button);
            backward_button = findViewById(R.id.backward_button);
            counter_tv = findViewById(R.id.counter_tv);
            length_tv = findViewById(R.id.length_tv);

            String path = getFilesDir().getAbsolutePath() + "/"+ fileName;
//            mediaPlayer = MediaPlayer.create(this, R.raw.sample);
            mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Exception of type : " + e.toString());
                e.printStackTrace();
            }

            seekbar = findViewById(R.id.seekBar);
            seekbar.setClickable(false);
            pause_button.setEnabled(false);

            play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();

                    finalTime = mediaPlayer.getDuration();
                    startTime = mediaPlayer.getCurrentPosition();
                    seekbar.setMax((int) finalTime);

                    length_tv.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) finalTime), TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                    );

                    counter_tv.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                    );

                    seekbar.setProgress((int) startTime);
                    myHandler.postDelayed(UpdateSongTime, 100);
                    pause_button.setEnabled(true);
                    play_button.setEnabled(false);
                }
            });

            pause_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Pausing audio", Toast.LENGTH_SHORT).show();
                    mediaPlayer.pause();
                    pause_button.setEnabled(false);
                    play_button.setEnabled(true);
                }
            });

            forward_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = (int) startTime;

                    if ((temp + forwardTime) <= finalTime) {
                        startTime = startTime + forwardTime;
                        mediaPlayer.seekTo((int) startTime);
                        Toast.makeText(getApplicationContext(), "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            backward_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = (int) startTime;

                    if ((temp - backwardTime) > 0) {
                        startTime = startTime - backwardTime;
                        mediaPlayer.seekTo((int) startTime);
                        Toast.makeText(getApplicationContext(), "You have Jumped backward 5 seconds", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

}
