package com.example.lenovo.iphonesave.bean;

/**
 * Created by Lenovo on 2017/6/30.
 */

public class Bean {

    /**
     * downloadurl : http://192.168.14.130:8080/xxx.apk
     * version : 2
     * desc : 这个是小码哥卫士的最新版本，赶紧来下载
     */
    public String downloadurl;
    public String version;
    public String desc;

    @Override
    public String toString() {
        return "Bean{" +
                "downloadurl='" + downloadurl + '\'' +
                ", version='" + version + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
