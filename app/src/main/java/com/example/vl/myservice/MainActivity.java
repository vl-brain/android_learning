package com.example.vl.myservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button startServiceButton;
    private Button stopServiceButton;
    private Button showServiceOutputButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startServiceButton = findViewById(R.id.start_service_button);
        startServiceButton.setOnClickListener(this);
        stopServiceButton = findViewById(R.id.stop_service_button);
        stopServiceButton.setOnClickListener(this);
        showServiceOutputButton = findViewById(R.id.show_service_output_button);
        showServiceOutputButton.setOnClickListener(this);
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
            case R.id.show_service_output_button:
                final Intent intent = new Intent(this, ServiceOutputActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void startMyService() {
        final Intent intent = new Intent(this, MyService.class);
        startService(intent);
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
    }

    private void stopMyService() {
        final Intent intent = new Intent(this, MyService.class);
        final boolean serviceStopped = stopService(intent);
        final String message = serviceStopped ? "Service stopped" : "Service not started yet";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
