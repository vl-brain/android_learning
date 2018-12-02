package com.example.vl.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.example.vl.app.base.State;
import com.example.vl.app.base.StateManager;

import java.util.*;

public class StateService extends IntentService {
    public static final String TAG = StateService.class.getSimpleName();
    public static final String SET_NEXT_STATE_ACTION = "com.example.vl.myservice.SET_NEXT_STATE";
    public static final String STATE_CHANGED_ACTION = "com.example.vl.myservice.ACTION_STATE_CHANGED";
    public static final String CURRENT_STATE_EXTRA = "CURRENT_STATE";
    public static final String BROADCAST_STATE_CHANGED_PERMISSION = "com.example.vl.app.BROADCAST_STATE_CHANGED";
    public static final List<State> ORDERED_STATES = Arrays.asList(State.values());
    private final StateManager stateManager = StateManager.getInstance();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public StateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String action = intent.getAction();
        if (action == null) {
            Log.w(TAG, "Skip intent without action!");
            return;
        }
        switch (action) {
            case SET_NEXT_STATE_ACTION:
                final State currentState = stateManager.getState();
                final State nextState = evaluateNextState(currentState);
                stateManager.setState(nextState);
                final Intent broadcastIntent = new Intent(STATE_CHANGED_ACTION);
                broadcastIntent.putExtra(CURRENT_STATE_EXTRA, nextState.name());
                broadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(broadcastIntent);
                break;
        }

    }

    private State evaluateNextState(State currentState) {
        if (currentState == null)
            return ORDERED_STATES.get(0);
        final int indexOfNextState = ORDERED_STATES.indexOf(currentState) + 1;
        return ORDERED_STATES.get(indexOfNextState < ORDERED_STATES.size() ? indexOfNextState : 0);
    }

}
