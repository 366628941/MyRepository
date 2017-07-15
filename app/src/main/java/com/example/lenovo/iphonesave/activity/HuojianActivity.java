package com.example.lenovo.iphonesave.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.example.lenovo.iphonesave.R;

public class HuojianActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huojian);
        ImageView view = (ImageView) findViewById(R.id.iv);
        view.setBackgroundResource(R.drawable.huojian);
        AnimationSet set = new AnimationSet(false);
        AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
        aa.setFillEnabled(false);
        int height = getWindowManager().getDefaultDisplay().getHeight();
        int width = getWindowManager().getDefaultDisplay().getWidth();
       // TranslateAnimation ta=new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
               // Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, height,
               // Animation.RELATIVE_TO_PARENT, 0);
        set.addAnimation(aa);
       // set.addAnimation(ta);
        set.setDuration(2000);
       view.startAnimation(set);
    }
}
