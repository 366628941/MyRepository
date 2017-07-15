package com.example.lenovo.iphonesave.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Lenovo on 2017/7/11.
 */

public class processinfo {
    private Drawable icon;
    private String appname;
    private  long memsize;
    private boolean usertask;

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    private boolean checked;
    private String packname;

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

    public long getMemsize() {
        return memsize;
    }

    public void setMemsize(long memsize) {
        this.memsize = memsize;
    }

    public boolean getUsertask() {
        return usertask;
    }

    public void setUsertask(boolean usertask) {
        this.usertask = usertask;
    }

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }


}
