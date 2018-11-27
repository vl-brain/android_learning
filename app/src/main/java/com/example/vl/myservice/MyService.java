package com.example.vl.myservice;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    public static final String SERVICE_TAG = "myService";
    public static final String TASK_TAG = "Async Counter";
    public static final String ASYNC_TASK_TAG = SERVICE_TAG + " " + TASK_TAG;
    public static final int MSG_SERVICE_OUTPUT = 1;
    public static final int MSG_SUBSCRIBE_ACTIVITY = 2;
    public static final int MSG_UNSUBSCRIBE_ACTIVITY = 3;

    private final List<Messenger> mActivityMessengers = new CopyOnWriteArrayList<>();
    private final Messenger mIncomeMessenger = new Messenger(new IncomeHandler());
    private final Thread task = new Thread(new Runnable() {
        @Override
        public void run() {
            int counter = 0;
            while (true) {
                if (Thread.interrupted())
                    break;
                final String outputMsg = "i = " + counter++;
                Log.d(ASYNC_TASK_TAG, outputMsg);
                for (Messenger mActivityMessenger : mActivityMessengers) {
                    try {
                        mActivityMessenger.send(Message.obtain(null, MSG_SERVICE_OUTPUT, outputMsg));
                    } catch (RemoteException e) {
                        Log.w(ASYNC_TASK_TAG, "Skip unavailable messenger", e);
                    }
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Log.e(ASYNC_TASK_TAG, "Interrupt counter when sleep", e);
                    break;
                }
            }
            stopSelf();
        }
    });

    private class IncomeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SUBSCRIBE_ACTIVITY:
                    mActivityMessengers.add(msg.replyTo);
                    break;
                case MSG_UNSUBSCRIBE_ACTIVITY:
                    mActivityMessengers.remove(msg.replyTo);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    public MyService() {
    }

    @Override
    public void onCreate() {
        Log.d(SERVICE_TAG, "onCreate");
        if (!task.isAlive()) {
            task.start();
        }
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.d(SERVICE_TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(SERVICE_TAG, "onBind");
        return mIncomeMessenger.getBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(SERVICE_TAG, "onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(SERVICE_TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(SERVICE_TAG, "onDestroy");
        task.interrupt();
        super.onDestroy();
    }

}
