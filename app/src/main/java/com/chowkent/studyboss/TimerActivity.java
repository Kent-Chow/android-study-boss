package com.chowkent.studyboss;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class TimerActivity extends ListActivity {
    private static final String[] items = {"Time1", "Time2", "Time3", "Time4", "Time5", "Time6", "Time7", "Time8", "Time9"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        setListAdapter(new TimerAdapter());
    }

    class TimerAdapter extends ArrayAdapter<String> {
        TimerAdapter() {
            super(TimerActivity.this, R.layout.timer_row, R.id.timer, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            final ViewHolder holder;

            if (row.getTag() == null) {
                holder = new ViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (ViewHolder)row.getTag();
            }

            holder.startButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    holder.timerValue.setText("Start!");
                    holder.pauseButton.setEnabled(true);
                    holder.startButton.setEnabled(false);
                }
            });

            holder.pauseButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    holder.timerValue.setText("Pause!");
                    holder.pauseButton.setEnabled(false);
                    holder.startButton.setEnabled(true);
                }
            });

            holder.resetButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    holder.timerValue.setText("Reset!");
                    holder.pauseButton.setEnabled(false);
                    holder.startButton.setEnabled(true);
                }
            });

            return row;
        }
    }
}
