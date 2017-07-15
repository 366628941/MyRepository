package com.example.lenovo.iphonesave.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.util.Date;

public class ClockService extends Service {
    public ClockService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Date date=new Date();
        int hours = date.getHours();
        int minutes = date.getMinutes();
        int seconds = date.getSeconds();
        Log.v("m520",hours+":"+minutes+":"+seconds);
        if(hours==15&&minutes==30&&seconds==0) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            //mediaPlayer.setDataSource(this,);
        }
        super.onCreate();
    }
}
