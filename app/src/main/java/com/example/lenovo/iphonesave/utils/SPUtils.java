package com.example.lenovo.iphonesave.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.lenovo.iphonesave.constant.Constans;

/**
 * SP的工具类,里面保存或者获取所有这个项目的值
 */

public class SPUtils {

    public static boolean getBoolean(Activity activity,String key){
        // 先获得Sp 参数1 保存的xml的文件名config,config.xml
        SharedPreferences sp = activity.getSharedPreferences(Constans.SP_NAME, Context.MODE_PRIVATE);
        // 得到Sp的boolean
        //参数1 保存的key 参数二 默认值,没有这个key的时候,给默认值
       return sp.getBoolean(key,false);
        // 返回这个boolean
    }
    public static void setBoolean(Activity activity,String key,boolean value){
        // 先获得Sp 参数1 保存的xml的文件名config,config.xml
        SharedPreferences sp = activity.getSharedPreferences(Constans.SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();

    }
    public static String getString(Context activity,String key){
        // 先获得Sp 参数1 保存的xml的文件名config,config.xml
        SharedPreferences sp = activity.getSharedPreferences(Constans.PASSWORD, Context.MODE_PRIVATE);
        // 得到Sp的boolean
        //参数1 保存的key 参数二 默认值,没有这个key的时候,给默认值
        return sp.getString(key,"");
        // 返回这个boolean
    }
    public static void setString(Activity activity,String key,String password){
        // 先获得Sp 参数1 保存的xml的文件名config,config.xml
        SharedPreferences sp = activity.getSharedPreferences(Constans.PASSWORD, Context.MODE_PRIVATE);
        sp.edit().putString(key,password).commit();

    }

    public static void setint(Context Service,String key,int value){
        // 先获得Sp 参数1 保存的xml的文件名config,config.xml
        SharedPreferences sp = Service.getSharedPreferences(Constans.XY, Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();

    }

    public static int getInt(Context Service, String key) {
        SharedPreferences sp = Service.getSharedPreferences(Constans.XY, Context.MODE_PRIVATE);
        return sp.getInt(key,0);
    }
}
