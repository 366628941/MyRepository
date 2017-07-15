package com.example.lenovo.iphonesave.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lenovo.iphonesave.R;

public class AdminActivity extends Activity {
private Button select_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initView();
        initListener();
    }

    private void initListener() {
        select_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this,SelectLocationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        select_location = (Button)findViewById(R.id.select_location);
    }
}
