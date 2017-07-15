package com.example.lenovo.iphonesave.service;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

import com.example.lenovo.iphonesave.constant.Constans;
import com.example.lenovo.iphonesave.utils.SPUtils;

public class LocationService extends Service {

    private LocationManager lm;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        //耗电量
        criteria.setPowerRequirement(Criteria.ACCURACY_HIGH);
        //精度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = lm.getBestProvider(criteria, true);

        lm.requestLocationUpdates(bestProvider, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //当经纬度发生变化的时候调用该方法
                //获取经纬度
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                String text=latitude+":"+longitude;
                //然后通过短信把经纬度发挥给安全号码
                SmsManager.getDefault().sendTextMessage(SPUtils.getString(getApplicationContext(), Constans.SAVAPHONE),text,null,null,null);
                //停止监听
                lm.removeUpdates(this);
                //停止服务
                stopSelf();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }
}
