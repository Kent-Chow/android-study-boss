package com.chowkent.studyboss;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TimerActivity extends ListActivity {
    private Handler customHandler = new Handler();
    private ArrayList<RowData> list;
    private TimerAdapter timerAdapter;

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
        Log.d("DEBUG", "onCreate() was called!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        startService(new Intent(this, TimerService.class));
        bindTimerService();
    }

    protected void init() {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();

        String json = appSharedPrefs.getString("list", "");
        Type type = new TypeToken<List<RowData>>() {}.getType();
        list = gson.fromJson(json, type);

        if (list == null) {
            list = new ArrayList<>();
        }

        timerAdapter = new TimerAdapter(list);
        setListAdapter(timerAdapter);
        getListView().setLongClickable(true);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setOnItemLongClickListener(new ActionModeHelper(this, getListView()));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindTimerService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timer_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add:
                addTimer();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        prefsEditor.putString("list", json);
        prefsEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindTimerService();
    }

    private RowData getRowData(int position) {
        return(((TimerAdapter)getListAdapter()).getItem(position));
    }

    private void addTimer() {
        list.add(new RowData(getResources().getString(R.string.timer_string)));
        timerService.addStopwatch();
        timerAdapter.notifyDataSetChanged();
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

    public boolean deleteTimerAction(int position) {
        list.remove(position);
        timerService.removeStopwatch(position);
        timerAdapter.notifyDataSetChanged();
        return true;
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
            if (timerService != null && timerService.checkIfExists(stopwatchId)) {
                rowData.timerValue = timerService.getFormattedElapsedTime(stopwatchId);
                holder.timerValue.setText(rowData.timerValue);
                customHandler.postDelayed(this, 0);
            }
        }
    }
}
