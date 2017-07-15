package com.example.lenovo.iphonesave.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.db.SelectLocationDao;

public class SelectLocationActivity extends Activity {
private EditText select_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        initView();

    }



    private void initView() {
        select_phone = (EditText)findViewById(R.id.select_phone);

    }


    public void click(View view) {
        String phone = select_phone.getText().toString();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(SelectLocationActivity.this, "号码不能为空！", Toast.LENGTH_SHORT).show();
        }else{
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("归属地为:");
            String location = SelectLocationDao.getLocation(SelectLocationActivity.this, phone);
            builder.setMessage(location);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }
}
