package com.example.vl.myservice.activity.base;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import com.example.vl.myservice.base.ServiceOutputCallback;
import com.example.vl.myservice.broadcast.receiver.ServiceOutputReceiver;
import com.example.vl.myservice.service.MyService;

public abstract class AbstractBroadcastOutputActivity extends AbstractOutputActivity implements View.OnClickListener {
    private ServiceOutputReceiver serviceOutputReceiver = new ServiceOutputReceiver(new ServiceOutputCallbackImpl());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(getOutputServiceIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(serviceOutputReceiver, new IntentFilter(MyService.NEXT_OUTPUT_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(serviceOutputReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(getOutputServiceIntent());
    }

    private class ServiceOutputCallbackImpl implements ServiceOutputCallback {
        @Override
        public void onNextOutput(String output) {
            appendOutputText(output);
        }
    }
}
