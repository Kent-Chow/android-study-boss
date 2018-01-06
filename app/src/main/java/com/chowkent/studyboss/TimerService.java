package com.chowkent.studyboss;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

public class TimerService extends Service {
    private static String TAG = "TimerService";

    public class LocalBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }

    private ArrayList<Stopwatch> stopwatches;
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

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.clear();
        prefsEditor.commit();

        stopwatches = new ArrayList<>();

        CharSequence text = getText(R.string.notification_string);
        CharSequence title = getText(R.string.app_name);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, TimerActivity.class), 0);

        notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .build();

        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void start(int i) {
        stopwatches.get(i).start();
    }

    public void pause(int i) {
        stopwatches.get(i).pause();
    }

    public void reset(int i) {
        stopwatches.get(i).reset();
    }

    public long getElapsedTime(int i) {
        return stopwatches.get(i).getElapsedTime();
    }

    public String getFormattedElapsedTime(int i) {
        long elapsedTime = getElapsedTime(i);
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

    public int addStopwatch() {
        int id = stopwatches.size();
        stopwatches.add(new Stopwatch());
        return id;
    }

    public void removeStopwatch(int i) {
        stopwatches.remove(i);
    }

    public boolean isRunning(int i) {;
        return stopwatches.get(i).isRunning();
    }

    public boolean checkIfExists(int i) {
        return i < stopwatches.size();
    }
}
