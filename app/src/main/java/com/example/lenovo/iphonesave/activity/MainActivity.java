package com.example.lenovo.iphonesave.activity;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.activity.saveactivity.FiveActivity;
import com.example.lenovo.iphonesave.activity.saveactivity.OneActivity;
import com.example.lenovo.iphonesave.adapter.MyAdapter;
import com.example.lenovo.iphonesave.constant.Constans;
import com.example.lenovo.iphonesave.service.ClockService;
import com.example.lenovo.iphonesave.utils.SPUtils;

public class MainActivity extends AppCompatActivity {

    private static final int ADMIN = 7;
    private static final int APPADMIN = 2;
    private static final int TASKMANAGER =3 ;
    private ImageView setting;
    private ImageView rl_iv;
    private GridView main_gv;
    private static final int SAVE=0;
    private static final int CALLSAVE =1;
    public int[] icons={R.mipmap.aa,R.mipmap.ab,
            R.mipmap.ac,R.mipmap.ad,
            R.mipmap.ae,R.mipmap.af
            ,R.mipmap.ah,R.mipmap.aj};
    public String[] title={"手机管家","通信卫士","软件管家","进程管理",
            "流量统计","病毒查杀","缓存清理","高级工具"};
    public String[] desc={"手机丢失好找","防骚扰反监听","方便管理软件","保持手机通畅",
            "注意流量超标","手机安全保障","手机快步如飞","特性处理更好"};
    private Context context;
    private EditText et_password;
    private TextView tv;
    private EditText et_password_conform;
    private Button btn_cancel;
    private Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        initView();
        initAnimation();
        initData();
        initListen();

        startService(new Intent(this, ClockService.class));
    }


    private void initListen() {
        //点击设置跳转界面
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        //给item设置点击事件  点击每一个Item就跳转相应的页面
        main_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    //private static final int SAVE=0;
                    case SAVE:
                        //先判断有没有保存密码
                        String password = SPUtils.getString(MainActivity.this, Constans.PASSWORD);
                        if(!TextUtils.isEmpty(password)){
                            Loginpassword();
                        }else{
                            Setpassword();
                        }

                        break;
                    case CALLSAVE:
                        Intent intent=new Intent(MainActivity.this,CallSaveActivity.class);
                        startActivity(intent);
                        break;
                    case ADMIN:
                        Intent intent1=new Intent(MainActivity.this,AdminActivity.class);
                        startActivity(intent1);
                        break;
                    case APPADMIN:
                        //点击软件管家的时候
                        Intent intent2=new Intent(MainActivity.this,AppManagerActivity.class);
                        startActivity(intent2);
                        break;
                    case TASKMANAGER:
                        //点击进程管家的时候
                        Intent intent3=new Intent(MainActivity.this,TaskManagerActivity.class);
                        startActivity(intent3);
                        break;
                }
            }
        });
    }

    private void Setpassword() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(), R.layout.set_password, null);
        et_password = (EditText) view.findViewById(R.id.dialog_et_password);
        et_password_conform = (EditText) view.findViewById(R.id.dialog_et_password_confirm);
        btn_cancel = (Button) view.findViewById(R.id.dialog_btn_cancel);
        btn_ok = (Button) view.findViewById(R.id.dialog_btn_ok);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        //取消的按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //确认的按钮
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_password.getText().toString();
                String password_confirm = et_password_conform.getText().toString();
                if(TextUtils.isEmpty(password)||TextUtils.isEmpty(password_confirm)) {
                    //只有有一个条件满足,都能进来
                    Toast.makeText(MainActivity.this, "请按要求输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                //说明两个密码都不为空,做相等的判断
                if(!password.equals(password_confirm)) {
                    //说明不相等
                    Toast.makeText(MainActivity.this, "密码输入不相等", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainActivity.this, "设置成功，请重新登录！", Toast.LENGTH_SHORT).show();
                SPUtils.setString(MainActivity.this,Constans.PASSWORD,password);
                dialog.dismiss();
                
            }
        });

    }

    private void Loginpassword() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(), R.layout.set_password, null);
        et_password = (EditText) view.findViewById(R.id.dialog_et_password);
        et_password_conform = (EditText) view.findViewById(R.id.dialog_et_password_confirm);
        tv = (TextView)view.findViewById(R.id.tv);
        //重新设置title
        tv.setText("请输入密码");
        //隐藏一个输入框
        et_password_conform.setVisibility(View.GONE);
        btn_cancel = (Button) view.findViewById(R.id.dialog_btn_cancel);
        btn_ok = (Button) view.findViewById(R.id.dialog_btn_ok);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        //取消的按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //确认的按钮
        btn_ok.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String newPassword = et_password.getText().toString();
                //获得保存在SP中的密码
                String oldPassword = SPUtils.getString(MainActivity.this, Constans.PASSWORD);

                if(TextUtils.isEmpty(newPassword)) {
                    Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //比对密码是否相等
                if(!newPassword.equals(oldPassword)) {
                    Toast.makeText(MainActivity.this, "密码输入错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                //说明密码输入正确
                // 关闭对话框,然后跳入到下一个环节(是否设置过向导,如果没有设置过,进入设置向导1,如果设置过,进入2)
                boolean setup = SPUtils.getBoolean(MainActivity.this, Constans.STAP);
                if(setup) {
                    //设置过
                    //进入最后一个向导页面
                    Intent intent=new Intent(MainActivity.this,FiveActivity.class);
                    startActivity(intent);
                }else {
                    //没有设置
                    // 进入设置向导1
                    Intent intent=new Intent(MainActivity.this,OneActivity.class);
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });

    }


    private void initData() {
        MyAdapter adapter=new MyAdapter(icons,title,desc,context);
        main_gv.setAdapter(adapter);
    }

    private void initAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(rl_iv, "rotationY", 0.0f, 360.0f);
        animator.setDuration(5000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();
    }

    private void initView() {
        rl_iv = (ImageView) findViewById(R.id.rl_iv);
        main_gv = (GridView)findViewById(R.id.main_gv);
        setting = (ImageView)findViewById(R.id.setting);
    }

}
