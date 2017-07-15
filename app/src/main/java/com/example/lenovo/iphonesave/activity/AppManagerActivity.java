package com.example.lenovo.iphonesave.activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.activity.saveactivity.ALLAPPActivity;
import com.example.lenovo.iphonesave.adapter.ManagerAdapter;
import com.example.lenovo.iphonesave.bean.AppInfo;
import com.example.lenovo.iphonesave.utils.GetApp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class AppManagerActivity extends Activity implements View.OnClickListener {
    private TextView tv1;
    private TextView tv2;
    private TextView frame_tv;
    private ListView manager_lv;
    private LinearLayout manager_ll;
    private AppInfo appInfo;
    private LinearLayout pupu_1;
    private LinearLayout pupu_2;
    private LinearLayout pupu_3;
    private LinearLayout pupu_4;
    private PopupWindow popupWindow;
    private ManagerAdapter adapter;
    private UnAPPReceiver receiver;
    private List<AppInfo> use;
    private List<AppInfo> system;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        initView();
        initData();
        initListener();
        //注册卸载的广播
        receiver = new UnAPPReceiver();
        IntentFilter Filter = new IntentFilter();
        Filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        Filter.addDataScheme("package");
        registerReceiver(receiver, Filter);
    }

    @Override
    protected void onDestroy() {
        if(popupWindow!=null){
            popupWindow.dismiss();
        }

        //界面销毁的时候反注册
        unregisterReceiver(receiver);
        receiver = null;
        super.onDestroy();
    }

    private void initListener() {
        //listView的滚动监听
        manager_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
              if(popupWindow!=null) {
                  popupWindow.dismiss();
              }
                if (use != null && system != null) {
                    if (firstVisibleItem > use.size()) {
                        frame_tv.setText("系统程序:" + system.size() + "个");
                    } else {
                        frame_tv.setText("用户程序:" + use.size() + "个");
                    }
                }
            }
        });

        //点击事件
        manager_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                appInfo = (AppInfo) adapter.getItem(position);
                // Log.v("m520","我被点击了");
                if (position == 0) {

                    return;
                } else if (position == use.size() + 1) {

                    return;
                } else if (position <= use.size()) {
                    int p = position - 1;
                    appInfo = use.get(p);
                } else {
                    int p = position - 1 - use.size() - 1;
                    appInfo = system.get(p);
                }
                // Log.v("m520",appInfo.getBackname());
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }

                //TextView tv =new TextView(getApplicationContext());
                // tv.setText(appInfo.getBackname());
                //tv.setTextColor(Color.RED);
                //自定义一个弹窗
                View popu = View.inflate(parent.getContext(), R.layout.popuwindow, null);
                pupu_1 = (LinearLayout) popu.findViewById(R.id.pupu_1);
                pupu_2 = (LinearLayout) popu.findViewById(R.id.pupu_2);
                pupu_3 = (LinearLayout) popu.findViewById(R.id.pupu_3);
                pupu_4 = (LinearLayout) popu.findViewById(R.id.pupu_4);
                //弹窗里面的点击监听事件
                pupu_1.setOnClickListener(AppManagerActivity.this);
                pupu_2.setOnClickListener(AppManagerActivity.this);
                pupu_3.setOnClickListener(AppManagerActivity.this);
                pupu_4.setOnClickListener(AppManagerActivity.this);
                popupWindow = new PopupWindow(popu, WRAP_CONTENT, WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//透明背景
                int[] location = new int[2];
                view.getLocationInWindow(location);
                popupWindow.showAtLocation(parent, Gravity.TOP + Gravity.LEFT, 60, location[1]);
                //给popu这只动画
                AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
                aa.setDuration(500);
                ScaleAnimation sa = new ScaleAnimation(0.2f, 1.0f, 0.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
                sa.setDuration(500);
                AnimationSet set = new AnimationSet(false);
                set.addAnimation(aa);
                set.addAnimation(sa);
                popu.startAnimation(set);

            }
        });

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<List<AppInfo>> lists= (List<List<AppInfo>>) msg.obj;
            use = lists.get(0);
            system = lists.get(1);
            Log.v("m520it",use.size()+":"+system.size());
            adapter = new ManagerAdapter(use, system);

            manager_lv.setAdapter(adapter);
            manager_ll.setVisibility(View.GONE);
        }
    };

    private void initData() {
        //拿到内存可用
        File dataDirectory = Environment.getDataDirectory();
        long freeSpace = dataDirectory.getFreeSpace();
        //拿到sd卡可用
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        long freeSpace1 = externalStorageDirectory.getFreeSpace();

        tv1.setText("内存可用:" + Formatter.formatFileSize(this, freeSpace));
        tv2.setText("sd卡可用:" + Formatter.formatFileSize(this, freeSpace1));

    }

    private void initView() {
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        frame_tv = (TextView) findViewById(R.id.frame_tv);
        manager_lv = (ListView) findViewById(R.id.manager_lv);
        manager_ll = (LinearLayout) findViewById(R.id.manager_ll);

        fillData();
    }

    private void fillData() {
        //加载进度条
        manager_ll.setVisibility(View.VISIBLE);
        new Thread() {
            public void run() {
                List<AppInfo> appinfo = GetApp.getAppinfo(getApplicationContext());
                List<List<AppInfo>> lists=new ArrayList<List<AppInfo>>();
                List<AppInfo> use=new ArrayList<AppInfo>();
                List<AppInfo> system=new ArrayList<AppInfo>();
                for (AppInfo info  : appinfo) {
                    if(info.getuserapp()){
                        use.add(info);
                    }else {
                        system.add(info);
                    }
                }
                lists.add(use);
                lists.add(system);
                Message msg=new Message();
                msg.obj=lists;
                handler.sendMessage(msg);


            }
        }.start();

    }

    //popu弹窗
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pupu_1://卸载
                downAPP();
                break;
            case R.id.pupu_2://启动
                openAPP();
                break;
            case R.id.pupu_3://分享
                shareAPP();
                break;
            case R.id.pupu_4://信息
                showAPPall();
                break;

        }
        popupWindow.dismiss();
    }

    //启动
    private void openAPP() {

        Intent intent = new Intent(AppManagerActivity.this, ALLAPPActivity.class);
        startActivity(intent);
    }


    //卸载
    private void downAPP() {

        if (appInfo.getuserapp()) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.DELETE");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse("package:" + appInfo.getBackname()));
            startActivity(intent);
        } else {
            Toast.makeText(AppManagerActivity.this, "系统程序需要Root", Toast.LENGTH_SHORT).show();
        }
    }


    //分享
    private void shareAPP() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "推荐你使用一款软件:" + appInfo.getAppname());
        startActivity(intent);
    }

    //信息
    private void showAPPall() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:" + appInfo.getBackname()));
        startActivity(intent);
    }

    //内部类  监听卸载的广播，打开界面的时候注册，关闭界面的时候反注册
    class UnAPPReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (appInfo.getuserapp()) {
                use.remove(appInfo);
            } else {
                system.remove(appInfo);
            }
            adapter.notifyDataSetChanged();
        }
    }


}
