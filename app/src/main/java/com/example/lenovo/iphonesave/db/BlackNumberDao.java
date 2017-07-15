package com.example.lenovo.iphonesave.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lenovo.iphonesave.bean.BlackBean;
import com.example.lenovo.iphonesave.constant.Constans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2017/7/7.
 */

public class BlackNumberDao {
    private BlackNumberHelper helper;
    private final SQLiteDatabase db;


    public BlackNumberDao(Context context) {

        this.helper = new BlackNumberHelper(context);
        db = this.helper.getWritableDatabase();
    }

    //增加
    public boolean add(String phone, String mode) {
        ContentValues values = new ContentValues();
        values.put(Constans.TABLE_PHONE, phone);
        values.put(Constans.TABLE_MODE, mode);
        long insert = db.insert(Constans.TABLE_NAME, null, values);
        db.close();
        if (insert != -1) {
            return true;
        } else {
            return false;
        }
    }

    //删除
    public boolean delete(String phone) {
        int delete = db.delete(Constans.TABLE_NAME, Constans.TABLE_PHONE + "=?", new String[]{phone});
        db.close();
        if (delete != 0) {
            return true;
        } else {
            return false;
        }
    }

    //修改
    public boolean updata(String Phone, String mode) {
        ContentValues Value = new ContentValues();
        Value.put(Constans.TABLE_MODE, mode);
        int update = db.update(Constans.TABLE_NAME, Value, Constans.TABLE_PHONE + "=?", new String[]{Phone});
        db.close();
        if (update != 0) {
            return true;
        } else {
            return false;
        }
    }

    //查询
    public List<BlackBean> selectAll() {
        List<BlackBean> list = new ArrayList<>();
        Cursor query = db.query(Constans.TABLE_NAME, new String[]{Constans.TABLE_PHONE, Constans.TABLE_MODE}, null, null, null, null, "_id desc");
        while (query.moveToNext()) {
            BlackBean bean = new BlackBean();
            bean.phone = query.getString(0);
            bean.mode = query.getString(1);
            list.add(bean);

        }
        query.close();
        db.close();
        return list;
    }

    //根据号码查询拦截模式的
    //设置里面开始黑名单的功能
    public String select(String phone) {
        String mode = "";
        Cursor query = db.query(Constans.TABLE_NAME, new String[]{Constans.TABLE_MODE}, Constans.TABLE_PHONE + "=?", new String[]{phone}, null, null, null);
        while (query.moveToNext()) {
            mode = query.getString(0);
        }
        query.close();
        db.close();
        return  mode;
    }


    public List<BlackBean> selectbufen(int maxcount, int startIndex) {
        List<BlackBean> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select  phone,mode from blacknum order by _id desc limit ? offset ?", new String[]{String.valueOf(maxcount), String.valueOf(startIndex)});
        while (cursor.moveToNext()){
            BlackBean bean = new BlackBean();
            bean.phone = cursor.getString(0);
            bean.mode = cursor.getString(1);
            list.add(bean);
        }
        cursor.close();
        db.close();
        return list;
    }
}
