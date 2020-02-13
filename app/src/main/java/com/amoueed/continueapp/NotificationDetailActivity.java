package com.amoueed.continueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amoueed.continueapp.db.AppDatabase;
import com.amoueed.continueapp.db.AppExecutors;
import com.amoueed.continueapp.db.LocalNotificationDataEntry;
import com.amoueed.continueapp.firebasemodel.NotificationDetailActivityModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NotificationDetailActivity extends AppCompatActivity {

    private Intent intent;
    private String fileName;
    private String content_identifier;
    private TextView info_text;
    private LinearLayout audio_control_ll;

    private ImageButton forward_button, pause_button, play_button, backward_button;
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

    private Button more_text_btn;
    private TextView info_0_text;
    int count_more_text_btn =0;
    private TextView info_1_text;
    private Button new_audio_btn;
    int count_new_audio_btn = 0;

    private String enter;
    private String exit;
    private String read_main = "No";
    private String read_0 = "No";
    private String read_1 = "No";
    private String notification;
    private DatabaseReference mDatabase;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        fileName = intent.getStringExtra("file_name");
        content_identifier = intent.getStringExtra("content_identifier");
        setContentView(R.layout.activity_notification_detail);
        setTitle("CoNTINuE");

        mDb = AppDatabase.getInstance(getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final LocalNotificationDataEntry task = mDb.localNotificationDataDao().loadEntryByFileName(fileName);
                task.setFlag(1);
                mDb.localNotificationDataDao().updateEntry(task);
            }
        });

        notification = fileName;
        syncWithFirebaseDatabase();

        info_text = findViewById(R.id.info_text);
        audio_control_ll = findViewById(R.id.audio_control_ll);

        more_text_btn = findViewById(R.id.more_text_btn);
        info_0_text = findViewById(R.id.info_0_text);
        info_1_text = findViewById(R.id.info_1_text);
        new_audio_btn = findViewById(R.id.new_audio_btn);

        // TODO remove Toast
        Toast.makeText(this,fileName+" "+content_identifier,Toast.LENGTH_LONG).show();

        if (getExtension(fileName).equals("txt")) {
            //logic to handle different notifications layout
            if(!fileName.equals("10.txt") && !fileName.equals("14.txt")){

                if(!content_identifier.contains("reminder")){
                    more_text_btn.setVisibility(View.VISIBLE);
                }else{
                    if(!fileName.equals("9.txt") && !fileName.equals("13.txt") && !fileName.equals("15.txt")){
                        more_text_btn.setVisibility(View.VISIBLE);
                    }
                }
            }

            more_text_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(count_more_text_btn == 0){
                        read_0 = "Yes";
                        info_0_text.setVisibility(View.VISIBLE);

                        String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
                        String newFileName = tokens[0].concat("_0.txt");

                        File file = new File(getFilesDir(), newFileName);
                        StringBuilder text = new StringBuilder();

                        try {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;

                            while ((line = br.readLine()) != null) {
                                text.append(line);
                                text.append('\n');
                            }
                            br.close();

                            info_0_text.setText(text);
                            count_more_text_btn++;
                        } catch (IOException e) {
                            Toast.makeText(NotificationDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }else if(count_more_text_btn == 1){
                        read_1 = "Yes";
                        info_1_text.setVisibility(View.VISIBLE);

                        String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
                        String newFileName = tokens[0].concat("_1.txt");

                        File file = new File(getFilesDir(), newFileName);
                        StringBuilder text = new StringBuilder();

                        try {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;

                            while ((line = br.readLine()) != null) {
                                text.append(line);
                                text.append('\n');
                            }
                            br.close();

                            info_1_text.setText(text);
                            count_more_text_btn++;
                            more_text_btn.setVisibility(View.GONE);
                        } catch (IOException e) {
                            Toast.makeText(NotificationDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                }
            });

            read_main = "Yes";

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
            read_main = "Yes";
            info_text.setVisibility(View.GONE);
            more_text_btn.setVisibility(View.GONE);
            audio_control_ll.setVisibility(View.VISIBLE);

            //logic to handle different notifications layout
            if(!fileName.equals("10.wav") && !fileName.equals("14.wav")){

                if(!content_identifier.contains("reminder")){
                    new_audio_btn.setVisibility(View.VISIBLE);
                }else{
                    if(!fileName.equals("9.wav") && !fileName.equals("13.wav") && !fileName.equals("15.wav")){
                        new_audio_btn.setVisibility(View.VISIBLE);
                    }
                }
            }

            new_audio_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.pause();
                    pause_button.setEnabled(false);
                    play_button.setEnabled(true);

                    if(count_new_audio_btn==0){
                        read_0 = "Yes";
                        String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
                        String newFileName = tokens[0].concat("_0.wav");

                        String path = getFilesDir().getAbsolutePath() + "/"+ newFileName;
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
                        count_new_audio_btn++;
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
                    else if(count_new_audio_btn==1){
                        read_1 = "Yes";
                        String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
                        String newFileName = tokens[0].concat("_1.wav");

                        String path = getFilesDir().getAbsolutePath() + "/"+ newFileName;
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
                        count_new_audio_btn++;
                        mediaPlayer.start();
                        finalTime = mediaPlayer.getDuration();
                        startTime = mediaPlayer.getCurrentPosition();
                        seekbar.setMax((int) finalTime);

                        length_tv.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) finalTime), TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                        );

                        counter_tv.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                        );
                        new_audio_btn.setVisibility(View.GONE);
                        seekbar.setProgress((int) startTime);
                        myHandler.postDelayed(UpdateSongTime, 100);
                        pause_button.setEnabled(true);
                        play_button.setEnabled(false);
                    }

                }
            });

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

    @Override
    protected void onStart() {
        super.onStart();

        enter = System.currentTimeMillis() +"";
    }

    @Override
    protected void onPause() {
        super.onPause();

        exit = System.currentTimeMillis() +"";

        if (getExtension(fileName).equals("wav")){
            mediaPlayer.pause();
            pause_button.setEnabled(false);
            play_button.setEnabled(true);
        }

        insertNotificationDetailActivityModelToFirebase();
    }

    private void syncWithFirebaseDatabase() {
        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }catch (Exception e){
            Log.e("NotificationDetail", e.getMessage());
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
    }

    private void insertNotificationDetailActivityModelToFirebase() {
        String[] tokens = notification.split("\\.(?=[^\\.]+$)");
        NotificationDetailActivityModel model = new NotificationDetailActivityModel(enter,exit,read_main, read_0, read_1,notification);
        SharedPreferences sharedPref = getSharedPreferences("content_identifier", Context.MODE_PRIVATE);
        String childMR = sharedPref.getString("mr_number","");
        mDatabase.child("app_data").child(childMR).child("NotificationDetailActivity").child(tokens[0]).push().setValue(model);
    }



    private String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

}
