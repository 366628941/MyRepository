package com.example.lenovo.iphonesave.activity.saveactivity;

import android.content.Intent;

import com.example.lenovo.iphonesave.R;

public class OneActivity extends BaseActivity {


    @Override
    protected void initData() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_one);
    }

    @Override
    public void showpre() {

    }

    @Override
    public void shownext() {
        startActivity(new Intent(this,TwoActivity.class));
        finish();

    }
}
