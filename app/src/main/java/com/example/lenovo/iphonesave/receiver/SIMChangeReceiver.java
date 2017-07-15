package com.example.lenovo.iphonesave.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.example.lenovo.iphonesave.constant.Constans;
import com.example.lenovo.iphonesave.utils.SPUtils;

public class SIMChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       //监听手机卡被改变的时候调用
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String newsim = tm.getSimSerialNumber();
        String oldsim = SPUtils.getString(context, Constans.SIM);
        if(!newsim.equals(oldsim)){
            SmsManager.getDefault().sendTextMessage(SPUtils.getString(context,Constans.SAVAPHONE),null,"sim change",null,null);
        }
    }
}
