package com.example.vl.myservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    public static final String TAG = MyService.class.getSimpleName();
    public static final int MSG_SERVICE_OUTPUT = 1;
    public static final int MSG_SUBSCRIBE_ACTIVITY = 2;
    public static final int MSG_UNSUBSCRIBE_ACTIVITY = 3;
    public static final String NEXT_OUTPUT_ACTION = "com.example.vl.myservice.service.NEXT_OUTPUT_ACTION";
    public static final String OUTPUT_MSG_EXTRA = "OUTPUT_MSG";
    private final List<Messenger> activityMessengers = new CopyOnWriteArrayList<>();
    private final Messenger incomeMessenger = new Messenger(new IncomeMsgHandler());
    private final Thread asyncTask = new Thread(new InternalAsyncTask());

    private class IncomeMsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SUBSCRIBE_ACTIVITY:
                    activityMessengers.add(msg.replyTo);
                    break;
                case MSG_UNSUBSCRIBE_ACTIVITY:
                    activityMessengers.remove(msg.replyTo);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private class InternalAsyncTask implements Runnable {
        private final String ASYNC_TASK_TAG = TAG + " " + "AsyncTask";

        @Override
        public void run() {
            int counter = 0;
            final List<Messenger> invalidMessengers = new ArrayList<>();
            while (true) {
                if (Thread.interrupted())
                    break;
                final String outputMsg = "i = " + counter++;
                Log.d(ASYNC_TASK_TAG, outputMsg);

                final Intent broadcastIntent = new Intent(NEXT_OUTPUT_ACTION);
                broadcastIntent.putExtra(OUTPUT_MSG_EXTRA, outputMsg);
                sendBroadcast(broadcastIntent);

                for (Messenger activityMessenger : activityMessengers) {
                    try {
                        activityMessenger.send(Message.obtain(null, MSG_SERVICE_OUTPUT, outputMsg));
                    } catch (RemoteException e) {
                        invalidMessengers.add(activityMessenger);
                        Log.w(ASYNC_TASK_TAG, "Skip unavailable messenger", e);
                    }
                }
                activityMessengers.removeAll(invalidMessengers);
                invalidMessengers.clear();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Log.e(ASYNC_TASK_TAG, "Interrupt counter when sleep", e);
                    break;
                }
            }
            stopSelf();
        }
    }

    @Override
    public void onCreate() {
        if (!asyncTask.isAlive()) {
            asyncTask.start();
        }
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return incomeMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        asyncTask.interrupt();
        super.onDestroy();
    }
}
