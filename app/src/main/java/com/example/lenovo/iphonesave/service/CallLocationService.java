package com.example.lenovo.iphonesave.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.constant.Constans;
import com.example.lenovo.iphonesave.db.SelectLocationDao;
import com.example.lenovo.iphonesave.utils.SPUtils;

public class CallLocationService extends Service {

    private TelephonyManager tm;
    private MyPhoneStateListener listener;
    private WindowManager mWM;
    private CallOutReceiver receiver;
    private View view;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyPhoneStateListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        //动态注册广播
        receiver = new CallOutReceiver();
        IntentFilter filter = new IntentFilter();
        //监听电话打出去开启广播
        filter.addAction("Intent.ACTION_NEW_OUTGOING_CALL");
        registerReceiver(receiver,filter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {

        tm.listen(listener,PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    class MyPhoneStateListener extends PhoneStateListener {


        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if(mWM!=null) {
                        mWM.removeView(view);
                        mWM=null;
                    }
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;

                case TelephonyManager.CALL_STATE_RINGING:
                    String location = SelectLocationDao.getLocation(CallLocationService.this, incomingNumber);
                    //Toast.makeText(CallLocationService.this,location , Toast.LENGTH_SHORT).show();
                    showMyToast(location);
                    break;

            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private void showMyToast(String location) {
        mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        //获取上次所保存的位置
        params.x = SPUtils.getInt(CallLocationService.this, Constans.X);
        params.y = SPUtils.getInt(CallLocationService.this, Constans.Y);
        params.gravity = Gravity.LEFT + Gravity.TOP;

        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        view = View.inflate(this, R.layout.mytoast, null);
        //给控件设置背景
        int[] icons=new int[]{R.drawable.call_locate_white,R.drawable.call_locate_orange,
                R.drawable.call_locate_blue,R.drawable.call_locate_gray,
                R.drawable.call_locate_green};
        int anInt = SPUtils.getInt(this, Constans.WHICH);
        view.setBackgroundResource(icons[anInt]);

        TextView tv = (TextView) view.findViewById(R.id.toast_tv);
        tv.setText(location);
        //给view设置触摸使其能够拖动
        view.setOnTouchListener(new View.OnTouchListener() {

            private float starty;
            private float startx;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN://手指放下
                        //获取当时的xy
                        startx = event.getRawX();
                        starty = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE://手指移动
                        //获取当前的xy
                        float newx = event.getRawX();
                        float newy = event.getRawY();
                        //计算移动的距离

                        int dx = (int) (newx- startx+0.5f);
                        int dy= (int) (newy- starty+0.5f);

                        //移动赋值
                        params.x+=dx;
                        params.y+=dy;
                        //设置不超出屏幕
                        if(params.x<0) {
                            params.x=0;
                        }
                        if(params.y<0) {
                            params.y=0;
                        }
                        if(params.x>mWM.getDefaultDisplay().getWidth()-v.getWidth()) {
                            params.x=mWM.getDefaultDisplay().getWidth()-v.getWidth();
                        }
                        if(params.y>mWM.getDefaultDisplay().getHeight()-v.getHeight()) {
                            params.y=mWM.getDefaultDisplay().getHeight()-v.getHeight();
                        }
                        //通知移动
                        mWM.updateViewLayout(view,params);

                        startx = event.getRawX();
                        starty = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP://手指抬起
                            //记录最后所在的位置
                        SPUtils.setint(CallLocationService.this,Constans.X,params.x);
                        SPUtils.setint(CallLocationService.this,Constans.Y,params.y);
                        break;
                }
                return false;
            }
        });
        mWM.addView(view, params);
    }
    class CallOutReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //开启广播就获取打出去的号码
            Log.v("m529","7777777777777777777777");
            String phone = intent.getDataString();
            String location = SelectLocationDao.getLocation(context, phone);
            showMyToast(location);
        }
    }
}
