package com.example.lenovo.iphonesave.activity.saveactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.lenovo.iphonesave.R;

/**
 * Created by Lenovo on 2017/7/3.
 */

public abstract class BaseActivity extends Activity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getRawX() - e2.getRawX() < 20) {
                    //向右滑动  点击的是pre
                    showpre();

                    overridePendingTransition(R.anim.pre_int, R.anim.pre_out);
                }

                if (e1.getRawX() - e2.getRawX() > 20) {
                    //向左滑动   next
                    shownext();
                    overridePendingTransition(R.anim.next_in, R.anim.next_out);
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    protected abstract void initData();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }

    public abstract void initView();

    public abstract void showpre();

    public abstract void shownext();


    public void pre(View v) {
        showpre();
        //翻页的时候覆盖默认的淡入淡出改成滑动进出
        overridePendingTransition(R.anim.pre_int, R.anim.pre_out);

    }

    public void next(View v) {
        shownext();
        overridePendingTransition(R.anim.next_in, R.anim.next_out);

    }
}
