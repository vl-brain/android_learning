package com.example.vl.app.broadcast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.vl.app.base.State;
import com.example.vl.app.broadcast.receiver.base.StateChangedCallback;
import com.example.vl.app.service.StateService;

public class StateBroadcastReceiver extends BroadcastReceiver {
    private StateChangedCallback stateChangedCallback;

    public StateBroadcastReceiver(StateChangedCallback stateChangedCallback) {
        this.stateChangedCallback = stateChangedCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action == null)
            return;
        switch (action) {
            case StateService.STATE_CHANGED_ACTION:
                final String currentState = intent.getStringExtra(StateService.CURRENT_STATE_EXTRA);
                stateChangedCallback.onStateUpdate(State.valueOf(currentState));
                break;
        }
    }
}
