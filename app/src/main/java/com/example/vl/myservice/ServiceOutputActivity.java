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
    public static final String TAG = ServiceOutputActivity.class.getSimpleName();
    public static final String ERR_MSG_FAILED_TO_SUBSCRIBE_TO_MY_SERVICE = "Failed to subscribe to MyService";
    public static final String ERR_MSG_FAILED_TO_UNSUBSCRIBE_FROM_MY_SERVICE = "Failed to unsubscribe from MyService";
    private Button backButton;
    private TextView serviceOutputTextView;
    private Messenger outputMessenger;
    private final Messenger incomeMessenger = new Messenger(new IncomeMsgHandler());
    private ServiceConnection connectionHandler = new ServiceConnectionHandler();

    private class IncomeMsgHandler extends Handler {
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

    private class ServiceConnectionHandler implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            outputMessenger = new Messenger(service);
            subscribeToMyServiceOutput();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            outputMessenger = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_output);
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(this);
        serviceOutputTextView = findViewById(R.id.service_output);
        serviceOutputTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, connectionHandler, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unsubscribeFromMyServiceOutput();
        unbindService(connectionHandler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
        }
    }

    private void subscribeToMyServiceOutput() {
        if (outputMessenger == null) {
            Log.w(TAG, ERR_MSG_FAILED_TO_SUBSCRIBE_TO_MY_SERVICE);
            return;
        }
        try {
            Message msg = Message.obtain(null, MyService.MSG_SUBSCRIBE_ACTIVITY);
            msg.replyTo = incomeMessenger;
            outputMessenger.send(msg);
        } catch (RemoteException e) {
            Log.e(TAG, ERR_MSG_FAILED_TO_SUBSCRIBE_TO_MY_SERVICE, e);
        }
    }

    private void unsubscribeFromMyServiceOutput() {
        if (outputMessenger == null) {
            Log.w(TAG, ERR_MSG_FAILED_TO_UNSUBSCRIBE_FROM_MY_SERVICE);
            return;
        }
        try {
            Message msg = Message.obtain(null, MyService.MSG_UNSUBSCRIBE_ACTIVITY);
            msg.replyTo = incomeMessenger;
            outputMessenger.send(msg);
        } catch (RemoteException e) {
            Log.e(TAG, ERR_MSG_FAILED_TO_UNSUBSCRIBE_FROM_MY_SERVICE, e);
        }
    }
}
