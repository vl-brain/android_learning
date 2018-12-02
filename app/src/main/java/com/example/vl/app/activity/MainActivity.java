package com.example.vl.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.vl.app.*;
import com.example.vl.app.base.State;
import com.example.vl.app.base.StateManager;
import com.example.vl.app.broadcast.receiver.StateBroadcastReceiver;
import com.example.vl.app.broadcast.receiver.base.StateChangedCallback;
import com.example.vl.app.service.StateService;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button nextStateButton;
    private TextView currentStateTextView;
    private StateBroadcastReceiver stateBroadcastReceiver;
    private IntentFilter stateIntentFilter;
    private StateManager stateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nextStateButton = findViewById(R.id.next_state_button);
        nextStateButton.setOnClickListener(this);
        currentStateTextView = findViewById(R.id.current_state_text_view);
        stateBroadcastReceiver = new StateBroadcastReceiver(new StateChangedCallbackImpl());
        stateIntentFilter = new IntentFilter(StateService.STATE_CHANGED_ACTION);
        stateManager = StateManager.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTextView(stateManager.getState());
        registerReceiver(stateBroadcastReceiver, stateIntentFilter,
                StateService.BROADCAST_STATE_CHANGED_PERMISSION, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(stateBroadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_state_button:
                final Intent intent = new Intent(this, StateService.class);
                intent.setAction(StateService.SET_NEXT_STATE_ACTION);
                startService(intent);
                break;
        }
    }

    private void updateTextView(State newState) {
        currentStateTextView.setText(newState.name());
    }

    private class StateChangedCallbackImpl implements StateChangedCallback {

        @Override
        public void onStateUpdate(State newState) {
            updateTextView(newState);
        }
    }

}
