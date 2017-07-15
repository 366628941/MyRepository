package com.example.lenovo.iphonesave.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.iphonesave.R;

/**
 * Created by Lenovo on 2017/7/10.
 */

public class SettingRelative extends RelativeLayout {

    private TextView bigtitle;
    private TextView smalltitle;

    public SettingRelative(Context context) {
        super(context);

    }

    public SettingRelative(Context context, AttributeSet attrs) {

        super(context, attrs);
        initView(context);
        initData(attrs);

    }

    private void initData(AttributeSet attrs) {
        String big = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "bigtitle");
        String small = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "smalltitle");
        bigtitle.setText(big);
        smalltitle.setText(small);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.relavite_view, this);
        bigtitle = (TextView) findViewById(R.id.myreltive_tv_bigtitle);
        smalltitle = (TextView) findViewById(R.id.myreltive_tv_smalltitle);

    }
    //暴露方法设置文本
    public void setsmalltitle(String string){
        smalltitle.setText(string);
    }


}
