package com.example.lenovo.iphonesave.activity.saveactivity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.constant.Constans;
import com.example.lenovo.iphonesave.utils.SPUtils;

public class ThreeActivity extends BaseActivity {
    private EditText et;

    @Override
    protected void initData() {
        String string = SPUtils.getString(ThreeActivity.this, Constans.SAVAPHONE);
        et.setText(string);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_three);
        et = (EditText) findViewById(R.id.three_et);
    }

    @Override
    public void showpre() {
        startActivity(new Intent(this, TwoActivity.class));
        finish();
    }

    @Override
    public void shownext() {
        //判空
        String text = et.getText().toString();
        if(TextUtils.isEmpty(text)){
            Toast.makeText(ThreeActivity.this, "请输入安全号码！", Toast.LENGTH_SHORT).show();
            return;
        }
        SPUtils.setString(ThreeActivity.this, Constans.SAVAPHONE,text);
        startActivity(new Intent(this, FourActivity.class));
        finish();

    }

    public void click(View view) {
        //点击访问系统联系人的按钮就跳转联系人的页面并且有数据返回
        Intent intent = new Intent(ThreeActivity.this, ThreePersonActivity.class);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data==null){
            return;
        }
        String phone = data.getStringExtra("phone");
        //设置到输入框
        et.setText(phone);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
