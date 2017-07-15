package com.example.lenovo.iphonesave.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 服务状态的工具类
 */

public class ServiceStateUtils {

    public static boolean getServiceState(Activity activity, String ServiceName) {
        boolean flag = false;
        ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo service : services) {
            String className = service.service.getClassName();
            if (ServiceName.equals(className)) {
                return true;
            }
        }
        return false;
    }
}
