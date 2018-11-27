package com.example.vl.myservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.text.Editable;
import android.text.Selection;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ServiceOutputActivity extends Activity implements View.OnClickListener {

    public static final String LOG_TAG = "Second activity";
    public static final String STATE_PARAM_IS_SERVICE_BOUND = "SERVICE_BOUND";
    public static final String STATE_PARAM_OUTPUT_MESSENGER = "OUTPUT_MESSENGER";
    private Button backButton;
    private TextView serviceOutputTextView;
    private boolean mServiceBound;
    private Messenger mOutputMessenger;
    private final Messenger mIncomeMessenger = new Messenger(new IncomeHandler());

    private class IncomeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyService.MSG_SERVICE_OUTPUT:
                    final String serviceOutput = (String) msg.obj;
                    serviceOutputTextView.append(serviceOutput + "\n");
                    final Editable editableText = serviceOutputTextView.getEditableText();
                    Selection.setSelection(editableText, editableText.length());
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mOutputMessenger = new Messenger(service);
            mServiceBound = true;
            subscribeToMyServiceOutput();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mOutputMessenger = null;
            mServiceBound = false;
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_PARAM_IS_SERVICE_BOUND, mServiceBound);
        outState.putParcelable(STATE_PARAM_OUTPUT_MESSENGER, mOutputMessenger);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(this);
        serviceOutputTextView = findViewById(R.id.service_output);
        serviceOutputTextView.setMovementMethod(new ScrollingMovementMethod());
        if (savedInstanceState != null) {
            mServiceBound = savedInstanceState.getBoolean(STATE_PARAM_IS_SERVICE_BOUND);
            mOutputMessenger = savedInstanceState.getParcelable(STATE_PARAM_OUTPUT_MESSENGER);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindToMyService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            unsubscribeFromMyServiceOutput();
            unbindFromMyService();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
        }
    }

    private void bindToMyService() {
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void subscribeToMyServiceOutput() {
        try {
            Message msg = Message.obtain(null, MyService.MSG_SUBSCRIBE_ACTIVITY);
            msg.replyTo = mIncomeMessenger;
            mOutputMessenger.send(msg);
        }
        catch (RemoteException e) {
            Log.e(LOG_TAG, "Failed to subscribe to MyService", e);
        }
    }

    private void unsubscribeFromMyServiceOutput() {
        try {
            Message msg = Message.obtain(null, MyService.MSG_UNSUBSCRIBE_ACTIVITY);
            msg.replyTo = mIncomeMessenger;
            mOutputMessenger.send(msg);
        }
        catch (RemoteException e) {
            Log.e(LOG_TAG, "Failed to unsubscribe from MyService", e);
        }
    }

    private void unbindFromMyService() {
        unbindService(mConnection);
        mServiceBound = false;
    }
}
