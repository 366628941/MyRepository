package com.example.lenovo.iphonesave.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;

import com.example.lenovo.iphonesave.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2017/7/10.
 */

public class GetApp {
    public static List<AppInfo> getAppinfo(Context context) {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> infoList = pm.getInstalledPackages(0);
        for (PackageInfo packinfo : infoList) {
            String packageName = packinfo.packageName;
            String appname = packinfo.applicationInfo.loadLabel(pm).toString();
            Drawable icon = packinfo.applicationInfo.loadIcon(pm);
            String path = packinfo.applicationInfo.sourceDir;
            File file = new File(path);
            long size = file.length();
            int flags = packinfo.applicationInfo.flags;
            AppInfo ai = new AppInfo();
            ai.setAppname(appname);
            ai.setBackname(packageName);
            ai.setIcon(icon);

            ai.setApksize(Formatter.formatFileSize(context, size));
            if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                //用户程序
                ai.setUserapp(true);


            } else {
                //系统程序
                ai.setUserapp(false);

            }
            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
                ai.setInrom(true);
            } else {
                ai.setInrom(false);
            }
            list.add(ai);
        }


        return list;

    }
}
