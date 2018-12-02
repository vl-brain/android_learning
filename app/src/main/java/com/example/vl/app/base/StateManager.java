package com.example.vl.app.base;


public class StateManager {
    private State state;

    private static class StateManagerHolder {
        private static final StateManager INSTANCE = new StateManager();
    }

    private StateManager() {
        this(State.A);
    }

    private StateManager(State state) {
        this.state = state;
    }

    public static StateManager getInstance() {
        return StateManagerHolder.INSTANCE;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
