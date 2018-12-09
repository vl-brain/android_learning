package com.example.vl.myservice.activity.base;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.*;
import android.util.Log;
import android.view.View;
import com.example.vl.myservice.service.MyService;

public abstract class AbstractBindOutputActivity extends AbstractOutputActivity implements View.OnClickListener {
    public static final String ERR_MSG_FAILED_TO_SUBSCRIBE_TO_MY_SERVICE = "Failed to subscribe to MyService";
    public static final String ERR_MSG_FAILED_TO_UNSUBSCRIBE_FROM_MY_SERVICE = "Failed to unsubscribe from MyService";
    public final String TAG = getClass().getSimpleName();

    private Messenger outputMessenger;
    private final Messenger incomeMessenger = new Messenger(new IncomeMsgHandler());
    private ServiceConnection connectionHandler = new ServiceConnectionHandler();

    private class IncomeMsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyService.MSG_SERVICE_OUTPUT:
                    appendOutputText((String) msg.obj);
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
    protected void onResume() {
        super.onResume();
        bindService(getOutputServiceIntent(), connectionHandler, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unsubscribeFromMyServiceOutput();
        unbindService(connectionHandler);
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
