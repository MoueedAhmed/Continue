package com.amoueed.continueapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;

import com.amoueed.continueapp.database.AppDatabase;
import com.amoueed.continueapp.database.AppExecutors;
import com.amoueed.continueapp.database.WeekEntry;
import com.amoueed.continueapp.ui.main.MainActivity;

import java.util.Date;

/**
 * Broadcast receiver for the alarm, which delivers the notification.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;
    // Notification ID.
    private static final int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    private AppDatabase mDb;
    private Context ctx;
    /**
     * Called when the BroadcastReceiver receives an Intent broadcast.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        ctx = context;
        mDb = AppDatabase.getInstance(ctx.getApplicationContext());

        SharedPreferences sharedPref = ctx.getSharedPreferences("count",Context.MODE_PRIVATE);
        int count = sharedPref.getInt("count",1);
        String extension;
        if(MainActivity.CONTENT_IDENTIFIER.equals("2")){
            extension = "wav";
        }else{
            extension = "txt";
        }
        if(count<=15){
            insertWeekEntry(extension,count);
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("count",count=count+1);
        editor.commit();

        // Deliver the notification.
        deliverNotification(context);
    }

    /**
     * Builds and delivers the notification.
     *
     * @param context, activity context.
     */
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
                .setContentText("Here is the content!")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        // Deliver the notification
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void insertWeekEntry(String ext, int week){
        Date date = new Date();
        String fileLocation = ctx.getFilesDir().getAbsolutePath()+"/"+week+"."+ext;
        final WeekEntry weekEntry = new WeekEntry(fileLocation, week, date);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.weekDao().insertWeek(weekEntry);
            }
        });
    }
}
