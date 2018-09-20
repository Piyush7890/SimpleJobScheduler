package com.piyush.jobscheduler;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class jobService extends JobService {

    NotificationManager manager;

    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    @Override
    public boolean onStartJob(JobParameters params) {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Job Service Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setDescription
                    ("Notifications from Job Service");
            manager.createNotificationChannel(channel);
        }

        PendingIntent intent = PendingIntent.getActivity(this,
                0,
                new Intent(this,MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID).setContentTitle("Job Service").setContentText("Your Job is running!").setContentIntent(intent).setSmallIcon(R.drawable.ic_autorenew_black_24dp).setAutoCancel(true);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
