package com.example.vl.myservice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.vl.myservice.R;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button showFirstActivityButton;
    private Button showSecondActivityButton;
    private Button showThirdActivityButton;
    private Button showFourthActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showFirstActivityButton = findViewById(R.id.show_linear_horizontal_layout_activity_button);
        showFirstActivityButton.setOnClickListener(this);
        showSecondActivityButton = findViewById(R.id.show_relative_layout_activity_button);
        showSecondActivityButton.setOnClickListener(this);
        showThirdActivityButton = findViewById(R.id.show_linear_vertical_layout_activity_button);
        showThirdActivityButton.setOnClickListener(this);
        showFourthActivityButton = findViewById(R.id.show_constraint_layout_activity_button);
        showFourthActivityButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_linear_horizontal_layout_activity_button:
                startActivity(new Intent(this, LinearHorizontalLayoutActivity.class));
                break;
            case R.id.show_relative_layout_activity_button:
                startActivity(new Intent(this, RelativeLayoutActivity.class));
                break;
            case R.id.show_linear_vertical_layout_activity_button:
                startActivity(new Intent(this, LinearVerticalLayoutActivity.class));
                break;
            case R.id.show_constraint_layout_activity_button:
                startActivity(new Intent(this, ConstraintLayoutActivity.class));
                break;
        }
    }

}
