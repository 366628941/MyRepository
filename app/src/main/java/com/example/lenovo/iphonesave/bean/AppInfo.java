package com.example.lenovo.iphonesave.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Lenovo on 2017/7/10.
 */

public class AppInfo {
    private Drawable icon;
    private String appname;
    private  String backname;
    private boolean inrom;
    private String apksize;
private boolean userapp;

    public String getApksize() {
        return apksize;
    }

    public void setApksize(String apksize) {
        this.apksize = apksize;
    }

    public boolean getuserapp() {
        return userapp;
    }

    public void setUserapp(boolean userapp) {
        this.userapp = userapp;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getBackname() {
        return backname;
    }

    public void setBackname(String backname) {
        this.backname = backname;
    }

    public boolean getInrim() {
        return inrom;
    }

    public void setInrom(boolean inrom) {
        this.inrom = inrom;
    }
}
