package com.example.vl.myservice.activity;

import android.view.ViewStub;
import com.example.vl.myservice.R;
import com.example.vl.myservice.activity.base.AbstractBindOutputActivity;

public class RelativeLayoutActivity extends AbstractBindOutputActivity implements ViewStub.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_relative_layout;
    }


}
