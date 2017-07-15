package com.example.lenovo.iphonesave.activity.saveactivity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.constant.Constans;
import com.example.lenovo.iphonesave.receiver.MyDeviceReceiver;
import com.example.lenovo.iphonesave.utils.SPUtils;
import com.example.lenovo.iphonesave.view.ItemRelative;

public class FourActivity extends BaseActivity {
    private ItemRelative four_irl;
    private TextView four_tv;
    private ComponentName componentName;
    private DevicePolicyManager dpm;

    @Override
    protected void initData() {
        boolean sava = SPUtils.getBoolean(FourActivity.this, Constans.SAVA);
        four_irl.setimage(sava);
        four_tv.setText(sava?"手机防盗已近开启":"手机防盗已近关闭");

        dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, MyDeviceReceiver.class);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_four);
        four_irl = (ItemRelative)findViewById(R.id.four_irl);
        four_tv = (TextView)findViewById(R.id.four_tv);

    }

    @Override
    public void showpre() {
        startActivity(new Intent(this,ThreeActivity.class));
        finish();
    }

    @Override
    public void shownext() {
        //跳转下一个页面就保存已经设置向导
        SPUtils.setBoolean(FourActivity.this,Constans.STAP,true);
        startActivity(new Intent(this,FiveActivity.class));
        finish();
    }

    public void click(View view) {
       //判断是否开启
        boolean sava = SPUtils.getBoolean(FourActivity.this, Constans.SAVA);
        four_irl.setimage(!sava);
        //保存
        SPUtils.setBoolean(FourActivity.this,Constans.SAVA,!sava);
        //设置对应的文本
        four_tv.setText(!sava?"手机防盗已近开启":"手机防盗已近关闭");

        if(sava==true){
            //说明开启放到保护那么就激活
            Intent intent=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"lalala");
        startActivity(intent);
        }else {
            dpm.removeActiveAdmin(componentName);
        }

    }
}
