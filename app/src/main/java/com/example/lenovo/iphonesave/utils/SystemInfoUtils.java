package com.example.lenovo.iphonesave.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.bean.processinfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2017/7/11.
 */

public class SystemInfoUtils {
    //获取所有的进程
    public static List<processinfo> getrunningprocessinfo(Context context) {
        List<processinfo> list = new ArrayList<>();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        PackageManager pm = context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {
            processinfo processinfo = new processinfo();
            String processName = info.processName;
            int i = am.getProcessMemoryInfo(new int[]{info.pid})[0].getTotalPrivateDirty() * 1024;
            processinfo.setMemsize(i);
            try {
                PackageInfo packageInfo = pm.getPackageInfo(processName, 0);
                String appname = packageInfo.applicationInfo.loadLabel(pm).toString();
                Drawable icon = packageInfo.applicationInfo.loadIcon(pm);
                String packageName = packageInfo.packageName;
                processinfo.setPackname(packageName);
                processinfo.setAppname(appname);
                processinfo.setIcon(icon);
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    //系统进程
                    processinfo.setUsertask(false);

                } else {
                    //用户进程
                    processinfo.setUsertask(true);
                }
            } catch (PackageManager.NameNotFoundException e) {
                processinfo.setAppname("进程");
                processinfo.setIcon(context.getResources().getDrawable(R.mipmap.save));
                e.printStackTrace();
            }
            list.add(processinfo);

        }


        return list;
    }

    //获取正在运行的进程数量
    public static int getrunningtask(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        return runningAppProcesses.size();
    }

    //获取内存信息
    public static long getAvailram(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outinfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outinfo);
        return outinfo.availMem;
    }

    public static long getTotalram(Context context) {
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        ActivityManager.MemoryInfo outinfo = new ActivityManager.MemoryInfo();
//        am.getMemoryInfo(outinfo);
//        return outinfo.totalMem;

        try {
            File file = new File("/proc/meminfo");
            FileInputStream stream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            String line = br.readLine();
            StringBuffer sb = new StringBuffer();
            for (char c : line.toCharArray()) {
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                }

            }
            return Long.parseLong(sb.toString()) * 1024;


        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
}
