package com.example.lenovo.iphonesave.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.iphonesave.R;

/**
 * Created by Lenovo on 2017/7/3.
 */

public class ItemRelative extends RelativeLayout {

    private TextView item_tv;
    private View view;
    private ImageView item_rl_iv;

    public ItemRelative(Context context) {
        super(context);
    }

    public ItemRelative(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initData(attrs);
    }

    private void initData(AttributeSet attrs) {
        String mytext = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "mytext");
        item_tv.setText(mytext);
    }

    public void setimage(boolean updata) {
        item_rl_iv.setImageResource(updata ? R.mipmap.on : R.mipmap.off);
    }

    private void initView(Context context) {

        view = View.inflate(getContext(), R.layout.item_rl, this);
        item_tv = (TextView) findViewById(R.id.item_rl_tv);
        item_rl_iv = (ImageView) findViewById(R.id.item_rl_iv);
    }

}
