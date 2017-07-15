package com.example.lenovo.iphonesave.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.constant.Constans;
import com.example.lenovo.iphonesave.service.CallLocationService;
import com.example.lenovo.iphonesave.service.CallSmsService;
import com.example.lenovo.iphonesave.service.huojianService;
import com.example.lenovo.iphonesave.utils.SPUtils;
import com.example.lenovo.iphonesave.utils.ServiceStateUtils;
import com.example.lenovo.iphonesave.view.ItemRelative;
import com.example.lenovo.iphonesave.view.SettingRelative;

public class SettingActivity extends Activity {
    private ItemRelative item_rl_root;
    private ItemRelative item_rl_root_black;
    private ItemRelative item_rl_root_huojian;
    private ItemRelative item_rl_root_location;
    private SettingRelative item_rl_bselect;
    private String[] names={"半透明","活力橙","卫士蓝","金属灰","苹果绿"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initData();
        initLinsen();

    }
    private void initView() {
        item_rl_root = (ItemRelative)findViewById(R.id.item_rl_root);
        item_rl_root_black = (ItemRelative)findViewById(R.id.item_rl_root_black);
        item_rl_root_location = (ItemRelative)findViewById(R.id.item_rl_root_location);
        item_rl_bselect = (SettingRelative)findViewById(R.id.item_rl_bselect);
        item_rl_root_huojian = (ItemRelative)findViewById(R.id.item_rl_root_huojian);
    }

    private void initData() {
        boolean aBoolean = SPUtils.getBoolean(SettingActivity.this, Constans.UPDATA);
        item_rl_root.setimage(aBoolean);
    }

    private void initLinsen() {
        item_rl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先获得当前的状态
                boolean  updata = SPUtils.getBoolean(SettingActivity.this, Constans.UPDATA);
                //然后换根据当前状态的取反换图片 点击之后状态就改变了所以取反
                item_rl_root.setimage(!updata);
                //然后保存

                SPUtils.setBoolean(SettingActivity.this,Constans.UPDATA,!updata);
                //然后回显
            }
        });
        item_rl_root_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取当时的状态 这里不能用sp保存和获取当前的状态，因为当服务挂掉的话就不能监听但是sp里面保存的不会个改变
                boolean serviceState = ServiceStateUtils.getServiceState(SettingActivity.this, "com.example.lenovo.iphonesave.service.CallSmsService");
                if(!serviceState){
                    Intent intent=new Intent(SettingActivity.this, CallSmsService.class);
                    startService(intent);
                }else{
                    Intent intent=new Intent(SettingActivity.this, CallSmsService.class);
                    stopService(intent);
                }
                item_rl_root_black.setimage(!serviceState);
                //回显应该在界面可见的时候写
            }
        });
        item_rl_root_huojian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean serviceState = ServiceStateUtils.getServiceState(SettingActivity.this, "com.example.lenovo.iphonesave.service.huojianService");
                if(!serviceState){
                    Intent intent=new Intent(SettingActivity.this, huojianService.class);
                    startService(intent);
                }else{
                    Intent intent=new Intent(SettingActivity.this, huojianService.class);
                    stopService(intent);
                }
                item_rl_root_huojian.setimage(!serviceState);
            }
        });
        item_rl_root_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean serviceState = ServiceStateUtils.getServiceState(SettingActivity.this, "com.example.lenovo.iphonesave.service.CallLocationService");
                if(!serviceState){
                    Intent intent=new Intent(SettingActivity.this, CallLocationService.class);
                    startService(intent);
                }else{
                    Intent intent=new Intent(SettingActivity.this, CallLocationService.class);
                    stopService(intent);
                }
                item_rl_root_location.setimage(!serviceState);
            }
        });
        //来电归属地背景选择
        item_rl_bselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("请选择背景:");
                int which = SPUtils.getInt(SettingActivity.this, Constans.WHICH);
                builder.setSingleChoiceItems(names, which, new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         item_rl_bselect.setsmalltitle(names[which]);
                         SPUtils.setint(SettingActivity.this,Constans.WHICH,which);
                         dialog.dismiss();
                     }
                 });
                builder.create().show();
            }

        });
    }

    @Override
    protected void onResume() {
        boolean serviceState = ServiceStateUtils.getServiceState(SettingActivity.this, "com.example.lenovo.iphonesave.service.CallSmsService");
        item_rl_root_black.setimage(serviceState);
        boolean serviceState1 = ServiceStateUtils.getServiceState(SettingActivity.this, "com.example.lenovo.iphonesave.service.CallLocationService");
        item_rl_root_location.setimage(serviceState1);
        boolean serviceState2 = ServiceStateUtils.getServiceState(SettingActivity.this, "com.example.lenovo.iphonesave.service.huojianService");
        item_rl_root_huojian.setimage(serviceState2);
        super.onResume();
    }
}
