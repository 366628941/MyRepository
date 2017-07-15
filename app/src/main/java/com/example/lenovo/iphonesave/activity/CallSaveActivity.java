package com.example.lenovo.iphonesave.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lenovo.iphonesave.R;
import com.example.lenovo.iphonesave.adapter.BlackNumberAdapter;
import com.example.lenovo.iphonesave.bean.BlackBean;
import com.example.lenovo.iphonesave.db.BlackNumberDao;

import java.util.List;

public class CallSaveActivity extends Activity {
    private ListView sbn_lv;
    private int maxcount = 20;
    private int startIndex = 0;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (adapter == null) {
                List<BlackBean> list = (List<BlackBean>) msg.obj;
                adapter = new BlackNumberAdapter(list);
                sbn_lv.setAdapter(adapter);
            }else {
                adapter.notifyDataSetChanged();
            }
        }

    };
    private BlackNumberAdapter adapter;
    private List<BlackBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_save);
        initView();
        initData();
        initListener();
    }

    //当listview滚动的时候这是监听
    private void initListener() {
        sbn_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            //滚动的状态回调的时候调用
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_IDLE://空闲的时候
                        //最后一条的位置
                        int position = view.getLastVisiblePosition();
                        //当前显示的item的总个数
                        int count = view.getCount();
                        if (position == (count - 1)) {
                            //空闲且滑动到最底部
                            //加载新的数据
                            startIndex += maxcount;
                            getdata(maxcount, startIndex);
                            //这里调用的时候list不为空了
                        }

                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initView() {
        sbn_lv = (ListView) findViewById(R.id.sbn_lv);

    }

    private void initData() {
        getdata(maxcount, startIndex);
        //调用这个方法的时候list为空，数据初始化

    }

    private void getdata(final int maxcount, final int startIndex) {
        new Thread() {
            public void run() {
                BlackNumberDao dao = new BlackNumberDao(CallSaveActivity.this);
                //每次查询二十条显示
                if(list==null) {
                    list = dao.selectbufen(maxcount, startIndex);
                }else{
                    list.addAll(dao.selectbufen(maxcount, startIndex));
                }
                Message msg = new Message();
                msg.obj = list;
                handler.sendMessage(msg);
            }
        }.start();
    }

    //点击添加的点击事件
    public void addBlackNumber(View view) {
        //点击添加的时候弹窗
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = View.inflate(this, R.layout.dialog_add_black, null);
        final EditText et_phone = (EditText) view1.findViewById(R.id.black_dialog_et_phone);
        final RadioGroup rg = (RadioGroup) view1.findViewById(R.id.black_dialog_rg);
        Button btn_cancel = (Button) view1.findViewById(R.id.black_dialog_btn_cancel);
        Button btn_ok = (Button) view1.findViewById(R.id.black_dialog_btn_ok);
        builder.setView(view1);
        final AlertDialog dialog = builder.create();
        dialog.show();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mode = null;
                String phone = et_phone.getText().toString();
                int id = rg.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.black_dialog_rb_phone:
                        mode = "0";
                        break;
                    case R.id.black_dialog_rb_sms:
                        mode = "1";
                        break;
                    case R.id.black_dialog_rb_all:
                        mode = "2";
                        break;
                }
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(CallSaveActivity.this, "请设置黑名单号码！", Toast.LENGTH_SHORT).show();
                    return;
                    //et_phone.setError("请设置黑名单号码！");
                } else {
                    Toast.makeText(CallSaveActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                    BlackBean bean = new BlackBean();
                    bean.phone = phone;
                    bean.mode = mode;
                    BlackNumberDao dao = new BlackNumberDao(CallSaveActivity.this);
                    boolean add = dao.add(phone, mode);
                    if (add) {
                        adapter.add(bean);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CallSaveActivity.this, "添加失败！", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            }
        });

    }
}
