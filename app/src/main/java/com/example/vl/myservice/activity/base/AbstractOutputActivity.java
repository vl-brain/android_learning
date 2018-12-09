package com.example.vl.myservice.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.vl.myservice.R;
import com.example.vl.myservice.service.MyService;

public abstract class AbstractOutputActivity extends Activity implements View.OnClickListener {
    private Button clearButton;
    private Button backButton;
    private TextView serviceOutputTextView;
    private Intent outputServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        outputServiceIntent = new Intent(this, MyService.class);
        setContentView(getLayoutId());
        clearButton = findViewById(getClearButtonId());
        clearButton.setOnClickListener(this);
        backButton = findViewById(getBackButtonId());
        backButton.setOnClickListener(this);
        serviceOutputTextView = findViewById(getServiceOutputTextViewId());
        serviceOutputTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    protected abstract int getLayoutId();

    protected int getClearButtonId() {
        return R.id.clear_button;
    }

    protected int getBackButtonId() {
        return R.id.back_button;
    }

    protected int getServiceOutputTextViewId() {
        return R.id.service_output_text_view;
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId == getBackButtonId()) {
            finish();
        } else if (viewId == getClearButtonId()) {
            clearOutputText();
        }
    }

    public void clearOutputText() {
        serviceOutputTextView.setText("");
    }

    public void appendOutputText(String output) {
        serviceOutputTextView.append(output + "\n");
        final Editable editableText = serviceOutputTextView.getEditableText();
        Selection.setSelection(editableText, editableText.length());
    }

    public Intent getOutputServiceIntent() {
        return outputServiceIntent;
    }
}
