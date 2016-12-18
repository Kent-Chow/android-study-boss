package com.chowkent.studyboss;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private Button startButton;
    private Button pauseButton;
    private Button resetButton;

    private TextView timerValue;

    private Handler customHandler = new Handler();

    private TimerService timerService;
    private ServiceConnection timerServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            timerService = ((TimerService.LocalBinder)service).getService();
            init();
            Log.d(TAG, "onServiceConnected() called!");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            timerService = null;
        }
    };

    private void bindTimerService() {
        bindService(new Intent(this, TimerService.class), timerServiceConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "bindTimerService() called!");
    }

    private void unbindTimerService() {
        if (timerServiceConnection != null) {
            unbindService(timerServiceConnection);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, TimerService.class));
        bindTimerService();
    }

    protected void init() {
        timerValue = (TextView) findViewById(R.id.timer);

        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                timerService.start();
                pauseButton.setEnabled(true);
                startButton.setEnabled(false);
                customHandler.postDelayed(updateTimerValue, 0);

            }
        });

        pauseButton = (Button) findViewById(R.id.pause_button);
        pauseButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                timerService.pause();
                pauseButton.setEnabled(false);
                startButton.setEnabled(true);

            }
        });

        resetButton = (Button) findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                timerService.reset();
                pauseButton.setEnabled(false);
                startButton.setEnabled(true);

            }
        });

        if (timerService != null && timerService.isRunning()) {
            Log.d("buttons", "timer is still running!");
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
        } else {
            Log.d("buttons", "timer is not running!");
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
        }

        customHandler.postDelayed(updateTimerValue, 0);
        Log.d(TAG, "Successfully created!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindTimerService();
    }

    private Runnable updateTimerValue = new Runnable() {
        @Override
        public void run() {
            if (timerService != null) {
                timerValue.setText(timerService.getFormattedElapsedTime());
                customHandler.postDelayed(this, 0);
            }
        }
    };
}

