package com.example.lenovo.iphonesave.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.example.lenovo.iphonesave.db.BlackNumberDao;
import com.example.lenovo.iphonesave.receiver.SMSReceiver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CallSmsService extends Service {

    private MyPhoneState litener;
    private TelephonyManager tm;
    private SMSReceiver receiver;
    private BlackNumberDao dao;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        dao = new BlackNumberDao(this);
        //服务开始的时候动态创建广播
        receiver = new SMSReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(receiver, intentFilter);
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //注册电话打进来的监听
        litener = new MyPhoneState();
        tm.listen(litener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //服务关闭的时候反注册广播
        unregisterReceiver(receiver);
        //取消来电号码的监听
        tm.listen(litener,PhoneStateListener.LISTEN_NONE);
        super.onDestroy();
    }

    private class MyPhoneState extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, final String incomingNumber) {
            switch (state){
                case  TelephonyManager.CALL_STATE_IDLE://空闲状态

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://接通状态
                    break;
                case TelephonyManager.CALL_STATE_RINGING://响铃状态
                    //如果这个时候是黑名单号码的电话拦截 立刻断电话

                    String mode = dao.select(incomingNumber);
                    if("0".equals(mode)||"2".equals(mode)) {
                        endCall();
                       /* new Thread(){
                            public void run(){
                                SystemClock.sleep(1000);

                            }
                        }.start();*/
                        //内容观察者,一观察到数据改变了,就开始调用删除的方法
                        Uri uri=Uri.parse("content://call_log/calls");
                        getContentResolver().registerContentObserver(uri, true, new ContentObserver(new Handler()) {
                            @Override
                            public void onChange(boolean selfChange) {
                                deleteCallLog(incomingNumber);
                                super.onChange(selfChange);
                            }
                        });

                    }
                    break;

            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private void deleteCallLog(String incomingNumber) {
        //删除通话记录，内容观察者
        ContentResolver resolver = getContentResolver();
        Uri uri=Uri.parse("content://call_log/calls");
        resolver.delete(uri,"number=?",new String[]{incomingNumber});
    }

    private void endCall() {
        try {
            //知道有这个ServiceManager 但是拿不到
            // IBinder b = ServiceManager.getService(TELEPHONY_SERVICE);
            //获得ServiceManager 字节码

            Class<?> loadClass = CallSmsService.this.getClassLoader().loadClass("android.os.ServiceManager");
            //获得对应的方法
            Method method = loadClass.getDeclaredMethod("getService", String.class);
            //调用该方法
            IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);

            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
            //想到ITelephony 里面的方法绝对要比TelephonyManager 的方法多,且级别高
            iTelephony.endCall();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
