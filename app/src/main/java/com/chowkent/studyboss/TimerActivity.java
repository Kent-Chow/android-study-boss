package com.chowkent.studyboss;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class TimerActivity extends ListActivity {
    private static final String[] items = {"Time1", "Time2", "Time3", "Time4", "Time5", "Time6", "Time7", "Time8", "Time9"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        ArrayList<RowData> list = new ArrayList<>();

        for (String s : items) {
            list.add(new RowData(s));
        }

        setListAdapter(new TimerAdapter(list));
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

            if (row.getTag() == null) {
                holder = new ViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (ViewHolder)row.getTag();
            }

            holder.startButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    rowData.timerValue = "Start!";
                    rowData.isPauseButtonEnabled = true;
                    rowData.isStartButtonEnabled = false;
                    updateButtons(holder, rowData);
                }
            });

            holder.pauseButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    rowData.timerValue = "Pause!";
                    rowData.isPauseButtonEnabled = false;
                    rowData.isStartButtonEnabled = true;
                    updateButtons(holder, rowData);
                }
            });

            holder.resetButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    rowData.timerValue = "Reset!";
                    rowData.isPauseButtonEnabled = false;
                    rowData.isStartButtonEnabled = true;
                    updateButtons(holder, rowData);
                }
            });

            updateButtons(holder, rowData);

            return row;
        }

        private void updateButtons(ViewHolder holder, RowData rowData) {
            holder.timerValue.setText(rowData.timerValue);
            holder.startButton.setEnabled(rowData.isStartButtonEnabled);
            holder.pauseButton.setEnabled(rowData.isPauseButtonEnabled);
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
}
