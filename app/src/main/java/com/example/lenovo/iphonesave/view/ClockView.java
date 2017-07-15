package com.example.lenovo.iphonesave.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.lenovo.iphonesave.R;

import java.util.Date;

/**
 * Created by Lenovo on 2017/7/15.
 */

public class ClockView extends View {

    private Paint paint;
    private int dialwidth;
    private int dialheight;
    private int hourwidth;
    private int hourheight;
    private int minwidth;
    private int minheight;
    private int secwidth;
    private int secheight;
    private int cenwidth;
    private int cenheight;
    private Bitmap dial;
    private Bitmap hour;
    private Bitmap min;
    private Bitmap sec;
    private Bitmap cen;
    private boolean clock=true;


    public ClockView(Context context) {
        this(context,null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        dial = BitmapFactory.decodeResource(getResources(), R.mipmap.clock_dial);
        dialwidth = dial.getWidth();
        dialheight = dial.getHeight();
        hour = BitmapFactory.decodeResource(getResources(), R.mipmap.hour_hand);
        hourwidth = hour.getWidth();
        hourheight = hour.getHeight();
        min = BitmapFactory.decodeResource(getResources(), R.mipmap.minute_hand);
        minwidth = min.getWidth();
        minheight = min.getHeight();
        sec = BitmapFactory.decodeResource(getResources(), R.mipmap.sec_hand);
        secwidth = sec.getWidth();
        secheight = sec.getHeight();
        cen = BitmapFactory.decodeResource(getResources(), R.mipmap.hand_center);
        cenwidth = cen.getWidth();
        cenheight = cen.getHeight();



        new Thread(){
            public void run(){
                while (clock) {
                    SystemClock.sleep(1000);
                    Log.v("m520","zaidhasjdfoia");
                    //不能用invalidate();  子线程
                    postInvalidate();
                }
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
       // int measuredWidth = getMeasuredWidth();
        //int measuredHeight = getMeasuredHeight();
        Date date = new Date();
        int hours = date.getHours();
        int minutes = date.getMinutes();
        int seconds = date.getSeconds();

        canvas.drawBitmap(dial,0,0,paint);
        canvas.save();
        canvas.rotate(hours*360/12+minutes/60f*360/12,dialwidth/2,dialheight/2);
        canvas.drawBitmap(hour,dialwidth/2-hourwidth/2,dialheight/2-hourheight+25,paint);
        canvas.restore();
        canvas.save();
        canvas.rotate(minutes*360/60,dialwidth/2,dialheight/2);
        canvas.drawBitmap(min,dialwidth/2-minwidth/2,dialheight/2-minheight+25,paint);
        canvas.restore();
        canvas.save();
        canvas.rotate(seconds*360/60,dialwidth/2,dialheight/2);
        canvas.drawBitmap(sec,dialwidth/2-secwidth/2,dialheight/2-secheight+25,paint);
        canvas.restore();
        canvas.drawBitmap(cen,dialwidth/2-cenwidth/2,dialheight/2-cenheight/2,paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modew = MeasureSpec.getMode(widthMeasureSpec);
        int sizew = MeasureSpec.getSize(widthMeasureSpec);
        int modeh = MeasureSpec.getMode(heightMeasureSpec);
        int sizeh = MeasureSpec.getSize(heightMeasureSpec);
        if(modew== MeasureSpec.AT_MOST){
            sizew=dialwidth;
        }
        if(modeh== MeasureSpec.AT_MOST){
            sizeh=dialheight;
        }
        setMeasuredDimension(sizew,sizeh);
    }
}
