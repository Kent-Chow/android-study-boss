package com.chowkent.studyboss;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class TimerActivity extends ListActivity {
    private static final String[] items = {"Time1", "Time2", "Time3", "Time4", "Time5", "Time6", "Time7", "Time8", "Time9"};
    private Handler customHandler = new Handler();

    private TimerService timerService;
    private ServiceConnection timerServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            timerService = ((TimerService.LocalBinder)service).getService();
            init();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            timerService = null;
        }
    };

    private void bindTimerService() {
        bindService(new Intent(this, TimerService.class), timerServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindTimerService() {
        if (timerServiceConnection != null) {
            unbindService(timerServiceConnection);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        startService(new Intent(this, TimerService.class));
        bindTimerService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindTimerService();
    }

    protected void init() {
        ArrayList<RowData> list = new ArrayList<>();
        for (String s : items) {
            list.add(new RowData(s));
            timerService.addStopwatch();
        }
        setListAdapter(new TimerAdapter(list));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timer_actions, menu);
        Log.d("MENU", "Menu inflated!");
        return super.onCreateOptionsMenu(menu);
    }

    private RowData getRowData(int position) {
        return(((TimerAdapter)getListAdapter()).getItem(position));
    }

    class TimerAdapter extends ArrayAdapter<RowData> {
        TimerAdapter(ArrayList<RowData> list) {
            super(TimerActivity.this, R.layout.timer_row, R.id.timer, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            final ViewHolder holder;
            final RowData rowData = getRowData(position);
            final int id = position;

            if (row.getTag() == null) {
                holder = new ViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (ViewHolder)row.getTag();
            }

            holder.startButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    timerService.start(id);
                    rowData.isPauseButtonEnabled = true;
                    rowData.isStartButtonEnabled = false;
                    updateButtons(holder, rowData, id);
                }
            });

            holder.pauseButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    timerService.pause(id);
                    rowData.isPauseButtonEnabled = false;
                    rowData.isStartButtonEnabled = true;
                    updateButtons(holder, rowData, id);
                }
            });

            holder.resetButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    timerService.reset(id);
                    rowData.isPauseButtonEnabled = false;
                    rowData.isStartButtonEnabled = true;
                    updateButtons(holder, rowData, id);
                }
            });

            updateButtons(holder, rowData, id);

            return row;
        }

        private void updateButtons(ViewHolder holder, RowData rowData, int id) {
            UpdateRunnable updateTimerValue = new UpdateRunnable(holder, rowData, id);
            holder.startButton.setEnabled(rowData.isStartButtonEnabled);
            holder.pauseButton.setEnabled(rowData.isPauseButtonEnabled);
            customHandler.postDelayed(updateTimerValue, 0);
        }
    }

    class RowData {
        public String timerValue;
        public boolean isStartButtonEnabled;
        public boolean isPauseButtonEnabled;
        public boolean isResetButtonEnabled;

        RowData(String timerValue) {
            this.timerValue = timerValue;
            isStartButtonEnabled = true;
            isPauseButtonEnabled = false;
            isResetButtonEnabled = true;
        }

        public String toString() {
            return timerValue;
        }
    }

    public class UpdateRunnable implements Runnable {
        private ViewHolder holder;
        private RowData rowData;
        private int stopwatchId;

        public UpdateRunnable(ViewHolder holder, RowData rowData, int id) {
            this.holder = holder;
            this.rowData = rowData;
            this.stopwatchId = id;
        }

        @Override
        public void run() {
            if (timerService != null) {
                rowData.timerValue = timerService.getFormattedElapsedTime(stopwatchId);
                holder.timerValue.setText(rowData.timerValue);
                customHandler.postDelayed(this, 0);
            }
        }
    }
}
