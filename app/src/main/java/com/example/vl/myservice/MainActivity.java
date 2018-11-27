package com.example.vl.myservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
    public static final String STATE_PARAM_SERVICE_NEED_STOP = "SERVICE_NEED_STOP";
    private Button startServiceButton;
    private Button stopServiceButton;
    private Button startSecondActivityButton;
    private boolean mServiceNeedStop = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_PARAM_SERVICE_NEED_STOP, mServiceNeedStop);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startServiceButton = findViewById(R.id.start_service_button);
        startServiceButton.setOnClickListener(this);
        stopServiceButton = findViewById(R.id.stop_service_button);
        stopServiceButton.setOnClickListener(this);
        startSecondActivityButton = findViewById(R.id.start_second_activity_button);
        startSecondActivityButton.setOnClickListener(this);
        if (savedInstanceState != null) {
            mServiceNeedStop = savedInstanceState.getBoolean(STATE_PARAM_SERVICE_NEED_STOP);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMyService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service_button:
                startMyService();
                break;
            case R.id.stop_service_button:
                stopMyService();
                break;

            case R.id.start_second_activity_button:
                final Intent intent = new Intent(this, ServiceOutputActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void startMyService() {
        if (mServiceNeedStop) {
            Toast.makeText(this, "Service already running", Toast.LENGTH_SHORT).show();
            return;
        }
        final Intent intent = new Intent(this, MyService.class);
        startService(intent);
        mServiceNeedStop = true;
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
    }

    private void stopMyService() {
        if (!mServiceNeedStop) {
            Toast.makeText(this, "Service not started yet", Toast.LENGTH_SHORT).show();
            return;
        }
        final Intent intent = new Intent(this, MyService.class);
        stopService(intent);
        mServiceNeedStop = false;
        Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
    }
}
