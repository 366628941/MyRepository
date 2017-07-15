package com.example.lenovo.iphonesave.activity.saveactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.constant.Constans;
import com.example.lenovo.iphonesave.utils.SPUtils;

public class FiveActivity extends Activity {
    private ImageView five_iv;
    private TextView five_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);
        initView();
        initData();

    }

    private void initData() {
        String string = SPUtils.getString(FiveActivity.this, Constans.SAVAPHONE);
        five_tv.setText(string);
        boolean sava = SPUtils.getBoolean(FiveActivity.this, Constans.SAVA);
        five_iv.setImageResource(sava ? R.mipmap.lock : R.mipmap.unlock);

    }

    private void initView() {
        five_tv = (TextView) findViewById(R.id.five_tv);
        five_iv = (ImageView) findViewById(R.id.five_iv);
    }

    public void click(View view) {
        startActivity(new Intent(FiveActivity.this,OneActivity.class));
        finish();
    }
}
