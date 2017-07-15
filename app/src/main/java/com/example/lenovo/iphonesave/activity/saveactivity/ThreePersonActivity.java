package com.example.lenovo.iphonesave.activity.saveactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.adapter.PersonAdapter;
import com.example.lenovo.iphonesave.bean.Person;
import com.example.lenovo.iphonesave.utils.PersonUtil;

import java.util.List;

public class ThreePersonActivity extends Activity {
private ListView contact_lv;
    private PersonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_person);
        initView();
        initData();
        //给item设置点击事件
        initLenson();
        
    }

    private void initLenson() {
        contact_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.v("m520","000000000000");
                Person person = (Person) adapter.getItem(position);
               // Log.v("m520",person.toString()+"1111111111");
                Intent intent=new Intent();
                intent.putExtra("phone",person.phone);
                setResult(0,intent);
                finish();
            }
        });
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            List<Person> list = (List<Person>) msg.obj;
            adapter = new PersonAdapter(list);
            //Log.v("m520",list.toString());
            contact_lv.setAdapter(adapter);
        }
    };

    private void initData() {
        //准备数据，在系统读取联系人数据库，耗时操作子线程
        new Thread(){
            public void run(){
                //写一个工具类获取系统的联系人
                List<Person> list = PersonUtil.getPersons(ThreePersonActivity.this);
                //传给主线程修改UI
                Message msg=new Message();
                msg.obj=list;
                handler.sendMessage(msg);
            }
        }.start();
        
    }

    private void initView() {
        contact_lv = (ListView)findViewById(R.id.contact_lv);

    }

    @Override
    protected void onPause() {
        Log.v("m520","onPause() wobei diaoyongle");

        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.v("m520","onStop() wobei diaoyongle");
        finish();
        super.onStop();
    }
}
