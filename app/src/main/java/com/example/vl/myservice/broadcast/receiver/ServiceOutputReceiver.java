package com.example.vl.myservice.broadcast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.vl.myservice.base.ServiceOutputCallback;
import com.example.vl.myservice.service.MyService;

public class ServiceOutputReceiver extends BroadcastReceiver {
    private ServiceOutputCallback callback;

    public ServiceOutputReceiver(ServiceOutputCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (MyService.NEXT_OUTPUT_ACTION.equals(intent.getAction())) {
            final String output = intent.getStringExtra(MyService.OUTPUT_MSG_EXTRA);
            callback.onNextOutput(output);
        }
    }
}
