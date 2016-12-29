package com.chowkent.studyboss;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ActionModeHelper implements ActionMode.Callback, AdapterView.OnItemLongClickListener {
    TimerActivity host;
    ActionMode activeMode;
    ListView modeView;

    ActionModeHelper(final TimerActivity host, ListView modeView) {
        this.host = host;
        this.modeView = modeView;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = host.getMenuInflater();

        inflater.inflate(R.menu.timer_context, menu);
        mode.setTitle(R.string.delete_timer);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        boolean result = host.deleteTimerAction(item.getItemId(), modeView.getCheckedItemPosition());

        if (item.getItemId() == R.id.delete) {
            activeMode.finish();
        }

        return result;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        activeMode = null;
        modeView.clearChoices();
        modeView.requestLayout();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        modeView.clearChoices();
        modeView.setItemChecked(position, true);

        if (activeMode == null) {
            activeMode = host.startActionMode(this);
        }

        return true;
    }
}
