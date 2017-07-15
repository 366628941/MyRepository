package com.example.lenovo.iphonesave.constant;

/**
 * Created by Lenovo on 2017/6/30.
 */

public class Constans {
    public static final String SP_NAME = "sate";
    public static final String UPDATA = "updata";
    public static final String PASSWORD = "password";
    public static final String STAP = "stap";
    public static final String SIM = "sim";
    public static final String SAVAPHONE = "savephone";
    public static final String SAVA = "sava";
    public static final String DB_NAME = "blacknumber.db";
    public static final String TABLE_NAME = "blacknum";
    public static final String TABLE_PHONE = "phone";
    public static final String TABLE_MODE = "mode";
    public static final String CREATE_TABLE = "create table " + TABLE_NAME +" (_id integer PRIMARY KEY AUTOINCREMENT, " + TABLE_PHONE + " vercher(20), " + TABLE_MODE + " vercher(20)) ";
    public static final String BLACKSTATE ="blackstate" ;
    public static final String X = "x";
    public static final String Y = "y";
    public static final String XY ="xy" ;
    public static final String WHICH = "which";


    public static class Url {
        public static final String PATH = "http://192.168.14.130:8080/";
        public static final String UPDATA = PATH + "info.json";
    }
}
