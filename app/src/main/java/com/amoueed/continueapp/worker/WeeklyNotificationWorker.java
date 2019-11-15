package com.amoueed.continueapp.worker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.amoueed.continueapp.R;
import com.amoueed.continueapp.db.AppDatabase;
import com.amoueed.continueapp.db.AppExecutors;
import com.amoueed.continueapp.db.LocalNotificationDataEntry;
import com.amoueed.continueapp.main.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

public class WeeklyNotificationWorker extends Worker {

    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private AppDatabase mDb;
    private Context ctx;

    public WeeklyNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        ctx = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        mNotificationManager = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mDb = AppDatabase.getInstance(ctx.getApplicationContext());

        SharedPreferences sharedPref =ctx.getSharedPreferences("content_identifier", Context.MODE_PRIVATE);
        String content_identifier = sharedPref.getString("content_identifier","");
        if(content_identifier.startsWith("t")){
            insertLocalNotificationData("txt");
        }
        else{
            insertLocalNotificationData("wav");
        }

        // Deliver the notification.
        deliverNotification(ctx);
        return Result.success();
    }

    private void deliverNotification(Context context) {
        // Create the content intent for the notification, which launches
        // this activity
        Intent contentIntent = new Intent(context, MainActivity.class);

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent
                        .FLAG_UPDATE_CURRENT);
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.title_logo)
                .setContentTitle("Notification")
                .setContentText("Kindly touch here or open application to view.")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        // Deliver the notification
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void insertLocalNotificationData(String extension) {
        SharedPreferences sharedPref = ctx.getSharedPreferences("count", Context.MODE_PRIVATE);
        int count = sharedPref.getInt("count",0);
        if(count<=8){
            if(extension.equals("txt")){
                String file_name = count+"."+extension;
                File file = new File(ctx.getFilesDir(), file_name);
                StringBuilder text = new StringBuilder();

                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                    br.close();
                    addLocalNotificationDataEntryToDB("CoNTINuE",text.substring(0,50)+"......",file_name);
                } catch (IOException e) {
                    Log.e("Worker",e.getMessage());
                }
            }
            else{
                String file_name = count+"."+extension;
                addLocalNotificationDataEntryToDB("CoNTINuE","You have audio message",file_name);
            }
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("count",count+1);
        editor.commit();
    }

    //create one row of LocalNotificationDataEntry in db
    private void addLocalNotificationDataEntryToDB(String notification_type, String content, String file_name){
        Date date = new Date();
        final LocalNotificationDataEntry entry = new LocalNotificationDataEntry
                (notification_type, content,file_name, date,0);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.localNotificationDataDao().insertEntry(entry);
            }
        });
    }
}
