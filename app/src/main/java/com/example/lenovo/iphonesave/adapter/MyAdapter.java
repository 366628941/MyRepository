package com.example.lenovo.iphonesave.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.iphonesave.R;

/**
 * Created by Lenovo on 2017/7/1.
 */

public class MyAdapter extends BaseAdapter {

    private final int[] mIcons;
    private final String[] mTitle;
    private final String[] mDesc;
    private final Context mContext;
    private ImageView logo;
    private TextView title;
    private TextView desc;

    public MyAdapter(int[] icons, String[] title, String[] desc, Context context) {
        this.mIcons = icons;
        this.mTitle = title;
        this.mDesc = desc;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mIcons != null ? mIcons.length : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.item_main_gv, null);
        logo = (ImageView) view.findViewById(R.id.main_item_logo);
        title = (TextView) view.findViewById(R.id.main_item_tv_title);
        desc = (TextView) view.findViewById(R.id.main_item_tv_desc);
        logo.setImageResource(mIcons[position]);
        title.setText(mTitle[position]);
        desc.setText(mDesc[position]);
        return view;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
