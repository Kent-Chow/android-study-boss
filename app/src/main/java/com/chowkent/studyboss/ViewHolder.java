package com.chowkent.studyboss;

import android.view.View;
import android.widget.Button;

public class ViewHolder {
    public Button startButton;
    public Button pauseButton;
    public Button resetButton;

    public ViewHolder(View row) {
        this.startButton = (Button)row.findViewById(R.id.start_button);
        this.pauseButton = (Button)row.findViewById(R.id.pause_button);
        this.resetButton = (Button)row.findViewById(R.id.reset_button);
    }
}
