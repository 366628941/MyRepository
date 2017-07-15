package com.example.lenovo.iphonesave.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Lenovo on 2017/7/8.
 */

public class SelectLocationDao {
    public static String getLocation(Context context, String phone) {
        String location = "没有查询到！";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getFilesDir().getAbsolutePath() + "/address.db", null, SQLiteDatabase.OPEN_READONLY);
        if (phone.matches("^1[35678]\\d{9}")) {
            //说明是手机号码
            Cursor cursor = db.rawQuery("select location from data2 where id=(select outkey from data1 where  id=?)", new String[]{phone.substring(0, 7)});
            while (cursor.moveToNext()) {
                location = cursor.getString(0);
            }
        } else switch (phone.length()) {
            case 3:
                if ("110".equals(phone)) {
                    location = "匪警";
                } else if ("120".equals(phone)) {
                    location = "急救";
                } else if ("119".equals(phone)) {
                    location = "火警";
                }
                break;

            case 4:
                location = "模拟器";
                break;
            case 5:
                location = "客服";
                break;
            case 7:
                //本地座机
                if (!phone.startsWith("0")) {
                    location = "本地号码";

                }
                break;
            case 8:
                //本地座机
                if (!phone.startsWith("0")) {
                    location = "本地号码";

                }

                break;
            default:
                if (phone.length() >= 10 && phone.startsWith("0")) {

                    Cursor cursor = db.rawQuery("select location from data2 where area=?", new String[]{phone.substring(1, 3)});
                    if (cursor.moveToNext()) {
                        //如果能够进来,说明查询到了位置
                        location = cursor.getString(0);
                    } else {
                        cursor = db.rawQuery("select location from data2 where area=?", new String[]{phone.substring(1, 4)});
                        cursor.moveToNext();
                        location = cursor.getString(0);

                    }

                    location = location.substring(0, location.length() - 2);

                }

                break;

        }
        return location;
    }
}
