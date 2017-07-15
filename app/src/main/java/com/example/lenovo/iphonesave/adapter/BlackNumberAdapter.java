package com.example.lenovo.iphonesave.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.bean.BlackBean;
import com.example.lenovo.iphonesave.db.BlackNumberDao;

import java.util.List;

/**
 * Created by Lenovo on 2017/7/7.
 */

public class BlackNumberAdapter extends BaseAdapter {
    private final List<BlackBean> mList;

    public BlackNumberAdapter(List<BlackBean> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(viewGroup.getContext(), R.layout.item_blacknumber_lv, null);
            holder.phone = (TextView) view.findViewById(R.id.black_item_tv_phone);
            holder.mode = (TextView) view.findViewById(R.id.black_item_tv_mode);
            holder.delete = (ImageView) view.findViewById(R.id.black_item_iv_delete);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        BlackBean bean = mList.get(i);
        holder.phone.setText(bean.phone);
        switch (bean.mode) {
            case "0":
                holder.mode.setText("电话拦截");
                break;
            case "1":
                holder.mode.setText("短信拦截");
                break;
            case "2":
                holder.mode.setText("全部拦截");
                break;
        }
        //删除事件
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewGroup.getContext());
                builder.setTitle("警告！");
                builder.setMessage("是否确定删除？");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //获取被选中要删除的item的数据

                        String phone = mList.get(i).phone;
                        //先删除数据库中的数据
                        BlackNumberDao dao = new BlackNumberDao(viewGroup.getContext());
                        boolean delete = dao.delete(phone);
                        if (delete) {
                            mList.remove(i);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(viewGroup.getContext(), "删除失败！", Toast.LENGTH_SHORT).show();

                        }

                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }
        });
        return view;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void add(BlackBean bean) {
        mList.add(0, bean);
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView phone;
        TextView mode;
        ImageView delete;
    }

}
