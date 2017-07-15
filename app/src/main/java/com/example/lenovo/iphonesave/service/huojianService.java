package com.example.lenovo.iphonesave.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.lenovo.iphonesave.activity.MainActivity;
import com.example.lenovo.iphonesave.view.ClockView;

public class huojianService extends Service {


    private WindowManager mWM;
   // private ImageView view;
    private ClockView view;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            WindowManager.LayoutParams params = (WindowManager.LayoutParams) msg.obj;
            mWM.updateViewLayout(view, params);

        }
    };
private long starttime= System.currentTimeMillis();
    private long newtime= System.currentTimeMillis();

    @Override
    public void onCreate() {
        mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        params.gravity = Gravity.LEFT + Gravity.TOP;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
       // view = new ImageView(this);
        //view.setBackgroundResource(R.drawable.huojian);
        view = new ClockView(this);

 //       AnimationDrawable background = (AnimationDrawable) view.getBackground();
 //       background.start();
        view.setOnTouchListener(new View.OnTouchListener() {
            public float starty;
            public float startx;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startx = event.getRawX();
                        starty = event.getRawY();

                        starttime =newtime;
                        newtime= System.currentTimeMillis();
                        if(newtime - starttime <500) {
                            Intent intent = new Intent(huojianService.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            return true;
                        }




                        break;
                    case MotionEvent.ACTION_MOVE:
                        float newx = event.getRawX();
                        float newy = event.getRawY();
                        int dx = (int) (newx - startx + 0.5f);
                        int dy = (int) (newy - starty + 0.5f);

                        //移动赋值
                        params.x += dx;
                        params.y += dy;
                        //设置不超出屏幕
                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y = 0;
                        }
                        if (params.x > mWM.getDefaultDisplay().getWidth() - v.getWidth()) {
                            params.x = mWM.getDefaultDisplay().getWidth() - v.getWidth();
                        }
                        if (params.y > mWM.getDefaultDisplay().getHeight() - v.getHeight()) {
                            params.y = mWM.getDefaultDisplay().getHeight() - v.getHeight();
                        }
                        //通知移动
                        mWM.updateViewLayout(view, params);
                        startx = event.getRawX();
                        starty = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        float rawY = event.getRawY();
                        if (rawY > mWM.getDefaultDisplay().getHeight() / 2) {
//                            Intent intent = new Intent(huojianService.this, HuojianActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);

                            new Thread() {
                                public void run() {
                                    for (int i = 0; i < 20; i++) {
                                        SystemClock.sleep(100);
                                        params.y -= i * 10;
                                        Message msg = new Message();
                                        msg.obj = params;
                                        handler.sendMessage(msg);
                                    }
                                }
                            }.start();
                        } else {
                            params.x = 0;
                            params.y = 0;
                            mWM.updateViewLayout(huojianService.this.view, params);
                        }
                        break;
                }
                return true;
            }
        });
        mWM.addView(view, params);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //服务停止的时候关闭服务
        mWM.removeView(view);
        view = null;
        super.onDestroy();
    }
}
