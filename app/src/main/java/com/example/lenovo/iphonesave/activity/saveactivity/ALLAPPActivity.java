package com.example.lenovo.iphonesave.activity.saveactivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.example.lenovo.iphonesave.R;

public class ALLAPPActivity extends AppCompatActivity {
    private GridView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allapp);
        gv = (GridView)findViewById(R.id.gv);
        PackageManager pm = getPackageManager();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
       // List<ResolveInfo> Infos = pm.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);

    }
}
