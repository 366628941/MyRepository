package com.example.lenovo.iphonesave.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2017/7/10.
 */

public class ManagerAdapter extends BaseAdapter {


    private final List<AppInfo> use;
    private final List<AppInfo> system;
    private List<AppInfo> lists;


    public ManagerAdapter(List<AppInfo> use, List<AppInfo> system) {
        this.use = use;
        this.system = system;
        Log.v("m520",use.size()+":"+system.size());
        lists = new ArrayList<>();

        for (AppInfo u : use) {
            lists.add(u);
        }
        for (AppInfo s : system) {
            lists.add(s);
        }
    }

    @Override
    public int getCount() {
        return lists != null ? lists.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null && convertView instanceof RelativeLayout) {
            holder = (ViewHolder) convertView.getTag();


        } else {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_manager, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.item_manager_lv);
            holder.tv1 = (TextView) convertView.findViewById(R.id.item_manager_ll_tv1);
            holder.tv2 = (TextView) convertView.findViewById(R.id.item_manager_ll_tv2);
            holder.tv3 = (TextView) convertView.findViewById(R.id.item_manager_ll_tv3);
            convertView.setTag(holder);
        }


        AppInfo appInfo = lists.get(position);

        if (position == 0) {
            TextView tv = new TextView(parent.getContext());
            tv.setText("用户程序:" + use.size() + "个");
            tv.setBackgroundColor(Color.GRAY);
            return tv;
        } else if (position == use.size() + 1) {
            TextView tv = new TextView(parent.getContext());
            tv.setText("系统程序:" + system.size() + "个");
            tv.setBackgroundColor(Color.GRAY);
            return tv;
        } else if (position <= use.size()) {
            int p = position - 1;
            appInfo = use.get(p);
        } else {
            int p = position - 1 - use.size() - 1;
            appInfo = system.get(p);
        }


        holder.iv.setImageDrawable(appInfo.getIcon());
        holder.tv1.setText(appInfo.getAppname());
        if (appInfo.getInrim()) {
            holder.tv2.setText("手机存储");
        } else {
            holder.tv2.setText("SD卡存储");
        }
        holder.tv3.setText(appInfo.getApksize());
        return convertView;
    }

    @Override
    public Object getItem(int position) {

        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        ImageView iv;
        TextView tv1;
        TextView tv2;
        TextView tv3;
    }

}
