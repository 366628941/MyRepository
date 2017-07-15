package com.example.lenovo.iphonesave.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.service.LocationService;

public class SMSReceiver extends BroadcastReceiver {

    private ComponentName mDeviceAdminSample;

    @Override
    public void onReceive(Context context, Intent intent) {
        //接收到短信就会被监听
        DevicePolicyManager dm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(context, MyDeviceReceiver.class);
        Object[] obj = (Object[]) intent.getExtras().get("pdus");
        //将obj转换为每条短信
        for (Object o : obj) {
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) o);
            String body = sms.getMessageBody();
            Log.v("m520", body);
            if ("#*alarm*#".equals(body)) {
                MediaPlayer mp = MediaPlayer.create(context, R.raw.fade);
                //设置循环
                mp.setLooping(true);
                //设置左声道和右声道
                mp.setVolume(1.0f, 1.0f);
                //开始播放音乐
                mp.start();
                //将短信终止
                abortBroadcast();
            } else if ("#*location*#".equals(body)) {
                //开启一个服务获取经纬度的服务
                Intent intent1 = new Intent(context, LocationService.class);
                context.startService(intent1);
                //将短信终止
                abortBroadcast();
            } else if ("#*lockscreent*#".equals(body)) {
                //锁屏  先在第四个页面获取高级管理激活
                if(dm.isAdminActive(componentName)){
                    //锁屏
                   dm.lockNow();
                    //设置开启密码
                    dm.resetPassword("123",0);
                }
                //将短信终止
                abortBroadcast();

            } else if ("#*wipedata*#".equals(body)) {
                if(dm.isAdminActive(componentName)){
                    dm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                }
                //将短信终止
                abortBroadcast();
            }
        }
    }
}
