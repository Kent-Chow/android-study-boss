package com.chowkent.studyboss;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class TimerService extends Service {
    private static String TAG = "TimerService";

    public class LocalBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }

    private Stopwatch stopwatch;
    private Notification notification;
    private LocalBinder binder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        stopwatch = new Stopwatch();

        CharSequence text = getText(R.string.notification_string);
        CharSequence title = getText(R.string.app_name);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .build();

        startForeground(1, notification);
        Log.d(TAG, "Successfully created!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void start() {
        stopwatch.start();
    }

    public void pause() {
        stopwatch.pause();
    }

    public void reset() {
        stopwatch.reset();
    }

    public long getElapsedTime() {
        return stopwatch.getElapsedTime();
    }

    public String getFormattedElapsedTime() {
        long elapsedTime = getElapsedTime();
        int secs = (int) (elapsedTime / 1000);
        int mins = secs / 60;
        int hours = mins / 60;
        int seconds100 = (int) ((elapsedTime / 10) % 100);
        secs = secs % 60;

        return (String.format("%02d", hours) + ":"
                + String.format("%02d", mins) + ":"
                + String.format("%02d", secs) + ":"
                + String.format("%02d", seconds100));
    }

    public boolean isRunning() {
        return stopwatch.isRunning();
    }

}
