package com.example.vl.app.broadcast.receiver.base;

import com.example.vl.app.base.State;

public interface StateChangedCallback {
    void onStateUpdate(State newState);
}
