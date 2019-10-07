package com.amoueed.continueapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.amoueed.continueapp.database.AppDatabase;
import com.amoueed.continueapp.database.AppExecutors;
import com.amoueed.continueapp.database.WeekEntry;
import com.amoueed.continueapp.ui.main.MainActivity;

import java.util.Date;

public class DailyWorker extends Worker {

    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    private AppDatabase mDb;
    private Context ctx;

    public DailyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        ctx = context;
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

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
                .setContentTitle("Daily")
                .setContentText("Data is syncing!")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        // Deliver the notification
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}