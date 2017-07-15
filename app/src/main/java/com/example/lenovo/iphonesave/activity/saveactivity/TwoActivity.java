package com.example.lenovo.iphonesave.activity.saveactivity;

import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.constant.Constans;
import com.example.lenovo.iphonesave.utils.SPUtils;

public class TwoActivity extends BaseActivity {


    private TelephonyManager tm;
    private ImageView two_iv;

    @Override
    protected void initData() {
        //得到电话管理器
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //回显
        //先得到状态
        String string = SPUtils.getString(TwoActivity.this, Constans.SIM);
        two_iv.setImageResource(TextUtils.isEmpty(string) ? R.mipmap.unlock : R.mipmap.lock);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_two);
        two_iv = (ImageView) findViewById(R.id.two_iv);
    }

    @Override
    public void showpre() {
        startActivity(new Intent(this, OneActivity.class));
        finish();
    }

    @Override
    public void shownext() {
        //判断如果没有绑定就不能跳转下一个页面
        String string = SPUtils.getString(TwoActivity.this, Constans.SIM);
        if(TextUtils.isEmpty(string)){
            Toast.makeText(TwoActivity.this, "请绑定SIM卡", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, ThreeActivity.class));
        finish();
    }

    public void click(View view) {
        //点击的时候先判断是否有绑定
        String sim = SPUtils.getString(TwoActivity.this, Constans.SIM);
        if (TextUtils.isEmpty(sim)) {
            //如果为空就说明没有绑定，那么就去获取sim卡号
            String number = tm.getSimSerialNumber();
            SPUtils.setString(TwoActivity.this, Constans.SIM, number);
            //换锁的图片
            two_iv.setImageResource(R.mipmap.lock);

        } else {
            //说明绑定过那么久解绑
            SPUtils.setString(TwoActivity.this, Constans.SIM, "");
            two_iv.setImageResource(R.mipmap.unlock);
        }

    }
}
