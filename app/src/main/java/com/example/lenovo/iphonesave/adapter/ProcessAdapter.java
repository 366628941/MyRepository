package com.example.lenovo.iphonesave.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.bean.processinfo;

import java.util.ArrayList;
import java.util.List;

import static com.example.lenovo.iphonesave.R.id.item_p_cb;

/**
 * Created by Lenovo on 2017/7/11.
 */

public class ProcessAdapter extends BaseAdapter {
    private final List<processinfo> mUse;
    private final List<processinfo> mSystem;
    private List<processinfo> mList;

    public ProcessAdapter(List<processinfo> use, List<processinfo> system) {
        this.mUse = use;
        this.mSystem = system;
        mList = new ArrayList<>();
        for (processinfo u : mUse) {
            mList.add(u);
        }
        for (processinfo s : mSystem) {
            mList.add(s);
        }

    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null && convertView instanceof RelativeLayout) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_process, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.item_p_iv);
            holder.tv1 = (TextView) convertView.findViewById(R.id.item_p_tv1);
            holder.tv2 = (TextView) convertView.findViewById(R.id.item_p_tv2);
            holder.cb = (CheckBox) convertView.findViewById(item_p_cb);
            convertView.setTag(holder);
        }
        processinfo processinfo = mList.get(position);
        if (position == 0) {
            TextView tv = new TextView(parent.getContext());
            tv.setText("用户进程:" + mUse.size() + "个");
            tv.setBackgroundColor(Color.GRAY);
            return tv;
        } else if (position == mUse.size() + 1) {
            TextView tv = new TextView(parent.getContext());
            tv.setText("系统进程:" + mSystem.size() + "个");
            tv.setBackgroundColor(Color.GRAY);
            return tv;
        } else if (position <= mUse.size()) {
            int p = position - 1;
            processinfo = mUse.get(p);
        } else {
            int p = position - 1 - mUse.size() - 1;

               processinfo=mSystem.get(p);


        }

        holder.iv.setImageDrawable(processinfo.getIcon());
        holder.tv1.setText(processinfo.getAppname());
        long memsize = processinfo.getMemsize();
        holder.tv2.setText("内存占用:" + Formatter.formatFileSize(parent.getContext(), memsize));
        holder.cb.setChecked(processinfo.getChecked());
        if (!TextUtils.isEmpty(processinfo.getPackname()) && !TextUtils.isEmpty(parent.getContext().getPackageName())) {
            if (processinfo.getPackname().equals(parent.getContext().getPackageName())) {
                holder.cb.setVisibility(View.INVISIBLE);
            } else {
                holder.cb.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        TextView tv1;
        TextView tv2;
        ImageView iv;
        CheckBox cb;
    }

}
