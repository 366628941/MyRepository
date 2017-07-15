package com.example.lenovo.iphonesave.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.bean.Bean;
import com.example.lenovo.iphonesave.constant.Constans;
import com.example.lenovo.iphonesave.service.huojianService;
import com.example.lenovo.iphonesave.utils.SPUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FirstActivity extends Activity {
    private RelativeLayout rl_root;
    private TextView tv_code;
    private TextView tv_text;
    private AnimationSet set;
    private Message msg;
    private long start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  先设置需要升级
       // SPUtils.setBoolean(this, Constans.UPDATA, true);
        boolean aBoolean = SPUtils.getBoolean(this, Constans.UPDATA);
       // Log.v("m520",aBoolean+"");
        initView();
        initData();
        initAnimotor();
        initListen();
        initDB();

    }



    private void initDB() {
        new Thread(){
            public void run(){

                    try {
                        File file=new File(getFilesDir(),"address.db");
                        if(file.exists()&&file.length()>0){
                            return;
                        }else{
                            InputStream open = getAssets().open("address.db");
                            FileOutputStream fos=new FileOutputStream(file);
                            byte[] b=new byte[1024];
                            int len=-1;
                            while ((len=open.read(b))!=-1){
                                fos.write(b,0,len);
                            }
                            fos.close();

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        }.start();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //Log.v("m520", "Handler=" + msg.what + "");
            switch ((int) msg.what) {
                case 1001:
                    Bean bean = (Bean) msg.obj;
                    showdialogupdata(bean);
                    break;
                case 1002:
                    startmainactivity();
                    break;
                default:
                    Toast.makeText(FirstActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                    startmainactivity();
            }
        }
    };

    //弹窗
    private void showdialogupdata(final Bean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否升级？");

        builder.setMessage(bean.desc);
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startmainactivity();
            }
        });
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击确定就去下载然后安装
                down(bean);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void down(Bean bean) {
        HttpUtils httpUtils = new HttpUtils();
        final File file = new File(Environment.getExternalStorageDirectory(), "苹果卫士.apk");
        //Log.v("m520",bean.downloadurl);
        // Log.v("m520",file.getAbsolutePath());
        httpUtils.download(bean.downloadurl, file.getAbsolutePath(), false, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {

                Toast.makeText(FirstActivity.this, "下载成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                startActivityForResult(intent, 1);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(FirstActivity.this, "下载失败！", Toast.LENGTH_SHORT).show();
                startmainactivity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        startmainactivity();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        tv_code = (TextView) findViewById(R.id.tv_code);
        tv_text = (TextView) findViewById(R.id.tv_text);
    }

    private void initAnimotor() {
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        RotateAnimation ra = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        set = new AnimationSet(false);
        set.addAnimation(aa);
        set.addAnimation(sa);
        set.addAnimation(ra);
        set.setDuration(3000);
        rl_root.startAnimation(set);
    }

    private void initData() {

        try {
            PackageInfo info = getPackageInfo();
            int versionCode = info.versionCode;
            String versionName = info.versionName;
            tv_code.setText(String.valueOf(versionCode));
            tv_text.setText(versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private PackageInfo getPackageInfo() throws PackageManager.NameNotFoundException {
        PackageManager packageManager = getPackageManager();
        return packageManager.getPackageInfo(getPackageName(), 0);
    }

    private void initListen() {
        //给动画设置监听
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (SPUtils.getBoolean(FirstActivity.this, Constans.UPDATA)) {
                    getdatabean();
                }else{

                }
            }


            @Override
            public void onAnimationEnd(Animation animation) {
                if (SPUtils.getBoolean(FirstActivity.this, Constans.UPDATA)) {

                }else{
                        startmainactivity();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startmainactivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getdatabean() {
        new Thread() {
            public void run() {
                try {
                    start = SystemClock.currentThreadTimeMillis();
                   // Log.v("m520", start + "");
                    msg = Message.obtain();
                    URL url = new URL(Constans.Url.UPDATA);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    if (conn.getResponseCode() == 200) {
                        // Log.v("m520it",conn.getResponseCode()+"");
                        InputStream inputStream = conn.getInputStream();
                        String read = read(inputStream);
                        //json解析
                        //Log.v("m520it",read);
                        Bean bean = pagamjson(read);
                        String version = bean.version;
                        if (Integer.valueOf(version) > getPackageInfo().versionCode) {
                            msg.what = 1001;
                            msg.obj = bean;
                        } else {
                            msg.what = 1002;
                        }
                    }
                } catch (IOException e) {
                    msg.what = 1003;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = 1004;
                    e.printStackTrace();

                } catch (PackageManager.NameNotFoundException e) {
                    msg.what = 1005;
                    e.printStackTrace();
                } finally {
                    long end = SystemClock.currentThreadTimeMillis();
                   // Log.v("m520", end + "");
                   // Log.v("m520", end - start + "");
                    // Log.v("m520it", msg.what + "1000000000001");
                    handler.sendMessageDelayed(msg, 3000 - (end - start));

                }

            }
        }.start();
    }

    private Bean pagamjson(String read) throws JSONException {
        Bean bean = new Bean();
        JSONObject jb = new JSONObject(read);

        bean.version = jb.getString("version");
        bean.downloadurl = jb.getString("downloadurl");
        bean.desc = jb.getString("desc");
        return bean;
    }

    private String read(InputStream inputStream) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String s = br.readLine();
        while (s != null) {
            sb.append(s);
            s = br.readLine();
        }
        return sb.toString();
    }


}
