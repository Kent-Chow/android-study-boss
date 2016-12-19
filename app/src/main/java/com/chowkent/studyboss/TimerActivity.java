package com.chowkent.studyboss;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class TimerActivity extends ListActivity {
    private static final String[] items = {"Time1", "Time2", "Time3", "Time4", "Time5", "Time6", "Time7", "Time8", "Time9"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
    }
}
