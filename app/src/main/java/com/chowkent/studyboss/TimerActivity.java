package com.chowkent.studyboss;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class TimerActivity extends ListActivity {
    private static final String[] items = {"Time1", "Time2", "Time3", "Time4", "Time5", "Time6", "Time7", "Time8", "Time9"};
    public ArrayList<RowModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        list = new ArrayList<>();

        for (String s : items) {
            list.add(new RowModel(s));
        }

        setListAdapter(new TimerAdapter(list));
    }

    private RowModel getModel(int position) {
        return(((TimerAdapter)getListAdapter()).getItem(position));
    }

    class TimerAdapter extends ArrayAdapter<RowModel> {
        TimerAdapter(ArrayList<RowModel> list) {
            super(TimerActivity.this, R.layout.timer_row, R.id.timer, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View row = super.getView(position, convertView, parent);
            final ViewHolder holder;
            final int pos = position;

            if (row.getTag() == null) {
                holder = new ViewHolder(row);
                row.setTag(holder);

                holder.startButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        holder.timerValue.setText("Start!");
                        holder.pauseButton.setEnabled(true);
                        holder.startButton.setEnabled(false);

                        RowModel model = getModel(pos);
                        model.setTimerValue("Start!");
                        model.setPauseButtonEnabled(true);
                        model.setStartButtonEnabled(false);
                    }
                });

                holder.pauseButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        holder.timerValue.setText("Pause!");
                        holder.pauseButton.setEnabled(false);
                        holder.startButton.setEnabled(true);

                        RowModel model = getModel(pos);
                        model.setTimerValue("Pause!");
                        model.setPauseButtonEnabled(false);
                        model.setStartButtonEnabled(true);
                    }
                });

                holder.resetButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        holder.timerValue.setText("Reset!");
                        holder.pauseButton.setEnabled(false);
                        holder.startButton.setEnabled(true);

                        RowModel model = getModel(pos);
                        model.setTimerValue("Reset!");
                        model.setPauseButtonEnabled(false);
                        model.setStartButtonEnabled(true);
                    }
                });
            } else {
                holder = (ViewHolder)row.getTag();
            }

            RowModel model = getModel(pos);
            holder.timerValue.setText(model.getTimerValue());
            holder.pauseButton.setEnabled(model.isPauseButtonEnabled());
            holder.startButton.setEnabled(model.isStartButtonEnabled());
            holder.resetButton.setEnabled(model.isResetButtonEnabled());

            return row;
        }
    }

    class RowModel {
        public String timerValue;
        public boolean startButtonEnabled;
        public boolean pauseButtonEnabled;
        public boolean resetButtonEnabled;

        RowModel(String timerValue) {
            this.timerValue = timerValue;
            startButtonEnabled = true;
            pauseButtonEnabled = false;
            resetButtonEnabled = true;
        }

        public String getTimerValue() {
            return timerValue;
        }

        public void setTimerValue(String timerValue) {
            this.timerValue = timerValue;
        }

        public boolean isStartButtonEnabled() {
            return startButtonEnabled;
        }

        public void setStartButtonEnabled(boolean startButtonEnabled) {
            this.startButtonEnabled = startButtonEnabled;
        }

        public boolean isPauseButtonEnabled() {
            return pauseButtonEnabled;
        }

        public void setPauseButtonEnabled(boolean pauseButtonEnabled) {
            this.pauseButtonEnabled = pauseButtonEnabled;
        }

        public boolean isResetButtonEnabled() {
            return resetButtonEnabled;
        }

        public void setResetButtonEnabled(boolean resetButtonEnabled) {
            this.resetButtonEnabled = resetButtonEnabled;
        }

        public String toString() {
            return timerValue;
        }
    }
}
