package com.example.lenovo.iphonesave.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lenovo.iphonesave.constant.Constans;


/**
 * Created by Lenovo on 2017/7/7.
 */

public class BlackNumberHelper extends SQLiteOpenHelper {

    public BlackNumberHelper(Context context) {
        super(context, Constans.DB_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constans.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
