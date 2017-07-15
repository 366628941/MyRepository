package com.example.lenovo.iphonesave.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.bean.Person;

import java.util.List;

/**
 * Created by Lenovo on 2017/7/4.
 */

public class PersonAdapter extends BaseAdapter{
    private final List<Person> mList;

    public PersonAdapter(List<Person> list) {
        this.mList=list;
    }

    @Override
    public int getCount() {
        return mList!=null?mList.size():0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(parent.getContext(), R.layout.item_person_lv,null);
            holder.name= (TextView) convertView.findViewById(R.id.contact_item_name);
            holder.phone= (TextView) convertView.findViewById(R.id.contact_item_phone);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        Person person = mList.get(position);
        holder.name.setText(person.name);
        holder.phone.setText(person.phone);
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
class ViewHolder{
    TextView name;
    TextView phone;
}

}
