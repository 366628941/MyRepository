package com.example.lenovo.iphonesave.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.example.lenovo.iphonesave.bean.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2017/7/4.
 */

public class PersonUtil {

    private static ContentResolver resolver;

    public static List<Person> getPersons(Context context) {
            //内容解析者
        List<Person> list=new ArrayList<Person>();
        resolver = context.getContentResolver();
        Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
        Uri uri2=Uri.parse("content://com.android.contacts/data");
        Cursor query = resolver.query(uri, new String[]{"_id"}, null, null, null);
        while (query.moveToNext()){
            Person person = null;
            String id = query.getString(0);
            if(!TextUtils.isEmpty(id)) {
                person=new Person();
                Cursor query1 = resolver.query(uri2, new String[]{"mimetype", "data1"}, "raw_contact_id=?", new String[]{id}, null);
                while (query1.moveToNext()){
                    String mimetype = query1.getString(0);
                    String data1 = query1.getString(1);
                    if(mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                        person.phone=data1;
                    }else  if(mimetype.equals("vnd.android.cursor.item/name")) {
                        person.name=data1;
                    }
                }

            }
            list.add(person);
        }
        return list;

    }
}
