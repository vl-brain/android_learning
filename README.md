# Task 4
Make IntentService.
It will handle income intents and change state in singleton StateManager.
StateManager will have 5 states(A->B->C->D->E) and 2 methods: getCurrentState and setNextState.
IntentService after change state should broadcast new state to all subscribers.
Activity which subscribed on broadcast message should view new state in TextView.
Activity should have button which send intent to IntentService for next state.

P.S. App need grant permission: 
    "com.example.vl.app.BROADCAST_STATE_CHANGED"