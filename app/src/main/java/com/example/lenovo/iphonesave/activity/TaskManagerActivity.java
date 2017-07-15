package com.example.lenovo.iphonesave.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.adapter.ProcessAdapter;
import com.example.lenovo.iphonesave.bean.processinfo;
import com.example.lenovo.iphonesave.utils.SystemInfoUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerActivity extends Activity implements View.OnClickListener {
    private TextView tv1;
    private TextView tv2;
    private TextView frame_tv;
    private ListView process_lv;
    private LinearLayout process_ll;
    private List<processinfo> use;
    private List<processinfo> system;
    private ProcessAdapter adapter;
    private Button bt_1;
    private Button bt_2;
    private Button bt_3;
    private Button bt_4;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            List<List<processinfo>> lists = (List<List<processinfo>>) msg.obj;
            use = lists.get(0);
            system = lists.get(1);
            adapter = new ProcessAdapter(use, system);
            process_lv.setAdapter(adapter);
            process_ll.setVisibility(View.GONE);
        }
    };
    private int getrunningtask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        initview();
        initdata();
        initlistener();
    }

    private void initview() {
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        process_lv = (ListView) findViewById(R.id.process_lv);
        process_ll = (LinearLayout) findViewById(R.id.process_ll);
        frame_tv = (TextView) findViewById(R.id.frame_tv);
        bt_1 = (Button) findViewById(R.id.bt_1);
        bt_2 = (Button) findViewById(R.id.bt_2);
        bt_3 = (Button) findViewById(R.id.bt_3);
        bt_4 = (Button) findViewById(R.id.bt_4);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        bt_3.setOnClickListener(this);
        bt_4.setOnClickListener(this);
    }

    private void initdata() {
        getrunningtask = SystemInfoUtils.getrunningtask(this);
        tv1.setText("运行中的进程:" + getrunningtask + "个");
        long availram = SystemInfoUtils.getAvailram(this);
        long totalram = SystemInfoUtils.getTotalram(this);
        tv2.setText("剩余/总内存:" + Formatter.formatFileSize(this, availram) + "/" + Formatter.formatFileSize(this, totalram));
        getDatas();


    }

    private void getDatas() {
        //准备所有进程的信息
        new Thread() {
            public void run() {
                List<processinfo> list = SystemInfoUtils.getrunningprocessinfo(TaskManagerActivity.this);
                List<List<processinfo>> lists = new ArrayList<List<processinfo>>();
                List<processinfo> use = new ArrayList<processinfo>();
                List<processinfo> system = new ArrayList<processinfo>();
                for (processinfo info : list) {
                    if (info.getUsertask()) {
                        use.add(info);
                    } else {
                        system.add(info);
                    }
                }
                lists.add(use);
                lists.add(system);
                Message msg = new Message();
                msg.obj = lists;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void initlistener() {
        process_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (use != null && system != null) {
                    if (firstVisibleItem > use.size()) {
                        frame_tv.setText("系统进程:" + system.size() + "个");
                    } else {
                        frame_tv.setText("用户进程:" + use.size() + "个");
                    }
                }
            }
        });
        //item 的点击事件
        process_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                processinfo info;
                if (position == 0) {
                    return;
                } else if (position == use.size() + 1) {
                    return;
                } else if (position <= use.size()) {
                    info = use.get(position - 1);
                } else {
                    info = system.get(position - 1 - use.size() - 1);
                }
                if(info.getPackname().equals(getPackageName())){
                    return;
                }
                if (info.getChecked()) {
                    info.setChecked(false);
                } else {
                    info.setChecked(true);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_1://全选
                selectAll();
                break;
            case R.id.bt_2://反选
                unselect();
                break;
            case R.id.bt_3://清理
                killselect();
                break;
            case R.id.bt_4://设置
                opensetting();
                break;
        }
    }
//设置
    private void opensetting() {
        Intent intent=new Intent(TaskManagerActivity.this,ProcessSettingActivity.class);
        startActivity(intent);
    }

    //清理
    private void killselect() {
        int count = 0;
        long savedmem = 0;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<processinfo> list = new ArrayList<>();
        for (processinfo user : use) {
            if (user.getChecked()) {
                am.killBackgroundProcesses(user.getPackname());
                count++;
                savedmem += user.getMemsize();
                list.add(user);
            }
        }
        for (processinfo sys : system) {
            if (sys.getChecked()) {
                am.killBackgroundProcesses(sys.getPackname());
                count++;
                savedmem += sys.getMemsize();
                list.add(sys);
            }
        }
        for (processinfo info : list) {
            if (info.getUsertask()) {
                use.remove(info);
            } else {
                system.remove(info);
            }
        }
        getDatas();
        String str = "清理了" + count + "个进程,释放了" + Formatter.formatFileSize(TaskManagerActivity.this, savedmem) + "的内存";
        Toast.makeText(TaskManagerActivity.this, str, Toast.LENGTH_SHORT).show();
        getrunningtask -= count;

    }

    //反选
    private void unselect() {
        for (processinfo user : use) {
            if (user.getPackname().equals(getPackageName())) {
                continue;
            }
            user.setChecked(!user.getChecked());
        }
        for (processinfo sys : system) {
            sys.setChecked(!sys.getChecked());
        }
        adapter.notifyDataSetChanged();
    }

    //全选
    private void selectAll() {
        for (processinfo user : use) {
            if (user.getPackname().equals(getPackageName())) {
                continue;
            }
            user.setChecked(true);
        }
        for (processinfo sys : system) {
            sys.setChecked(true);
        }
        adapter.notifyDataSetChanged();
    }
}
