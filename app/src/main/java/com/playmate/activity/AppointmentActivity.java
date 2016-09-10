package com.playmate.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.playmate.R;
import com.playmate.data.TaskAppointmentBean;
import com.playmate.util.HttpUtil;
import com.playmate.util.adapter.AppointmentListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppointmentActivity extends Activity implements AdapterView.OnItemClickListener {

    private Activity thisActivity;
    private String taskId;
    private long selfUserId;
    private long chooseId;
    private String eventTime;
    private String eventPrice;
    private int singlePrice;
    private String adress;
    private int timeLen;

    private ArrayList<TaskAppointmentBean> appointmenList;
    private AppointmentListAdapter appointmentListAdapter;
    private boolean[] isSelects;

    private ImageButton ib_back;
    private ListView lv_user;
    private TextView tv_payData;
    private TextView tv_numberOfAppointed;
    private Button button_pay;

    private int checkNum;
    private int totalMoney;

    private static final int SET_APPOINTMENT_USER = 1;

    private Handler handler = new Handler() {

        // 处理子线程给我们发送的消息。
        @Override
        public void handleMessage(android.os.Message msg) {

            if (msg.what == SET_APPOINTMENT_USER){
                thisActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 当数据改变时调用此方法通知view更新
//                        appointmentListAdapter.notifyDataSetChanged();
                        String totalMoneyStr = "￥" + totalMoney + ".00";
                        tv_numberOfAppointed.setText("已约" + checkNum + "人");
                        tv_payData.setText(totalMoneyStr);
                    }
                });
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        thisActivity = this;
        checkNum = 0;
        totalMoney = 0;
        chooseId = 0;
        try {
            Bundle bundle = this.getIntent().getBundleExtra("bundle");
            if (bundle == null) {
                Toast.makeText(this, "网络连接失败，请检测网络！", Toast.LENGTH_LONG).show();
                return;
            }
            taskId = bundle.getString("taskId", "");
            selfUserId = bundle.getLong("userId", 0);
            eventPrice = bundle.getString("eventPrice", "");
            eventTime = bundle.getString("eventTime", "");
            singlePrice = bundle.getInt("singlePirce", 0);
            adress = bundle.getString("adress", "");
            timeLen = bundle.getInt("timeLen", 1);

        } catch (Exception e) {
            taskId = "";
            selfUserId = 0;
        }

        initData();
        initView();
        setListener();
    }

    private void initView() {
        ib_back = (ImageButton) findViewById(R.id.activity_appointment_ib_back);
        lv_user = (ListView) findViewById(R.id.activity_appointment_lv_user);
        tv_payData = (TextView) findViewById(R.id.activity_appointment_tv_payData);
        tv_numberOfAppointed = (TextView) findViewById(R.id.activity_appointment_tv_numberOfAppointed);
        button_pay = (Button) findViewById(R.id.activity_appointment_button_pay);
    }

    private void initData() {
        appointmenList = new ArrayList<TaskAppointmentBean>();
        JSONObject jo = new JSONObject();
        try {
            jo.put("userId", selfUserId);
            jo.put("taskId", taskId);
        } catch (JSONException e) {
            Toast.makeText(this, "网络连接失败，请检测网络！", Toast.LENGTH_LONG).show();
        }

        getTaskAppointmentThread dataThread = new getTaskAppointmentThread(taskId, selfUserId, jo.toString());
        dataThread.start();
    }

    private void setListener(){
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisActivity.finish();
            }
        });

        button_pay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("taskId", taskId);
                bundle.putString("eventPrice", eventPrice);
                bundle.putString("eventTime", eventTime);
                bundle.putInt("singlePrice", singlePrice);
                bundle.putString("adress", adress);
                bundle.putInt("timeLen", timeLen);

                bundle.putLong("chooseId", chooseId);

                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

        lv_user.setOnItemClickListener(this);
    }

    private void setData(){
        appointmentListAdapter = new AppointmentListAdapter(thisActivity, appointmenList, isSelects);
        lv_user.setAdapter(appointmentListAdapter);
    }

    class getTaskAppointmentThread extends Thread {
        String mTaskId;
        long mUserId;
        String mDataJsonStr;

        public getTaskAppointmentThread(String taskid, long userId, String dataJsonStr) {
            mTaskId = taskid;
            mUserId = userId;
            mDataJsonStr = dataJsonStr;
        }

        @Override
        public void run() {
            String resultDate = HttpUtil.getHttpData("http://120.25.56.82:9100/task/listJoin", mDataJsonStr);
            try {
                JSONArray jsonArray = new JSONArray(resultDate);
                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject resultJson = jsonArray.getJSONObject(i);
                    TaskAppointmentBean taskAppointmentBean = new TaskAppointmentBean(resultJson);
                    appointmenList.add(taskAppointmentBean);
                    appointmenList.add(taskAppointmentBean);
                }
                isSelects = new boolean[appointmenList.size()];
                for (int i=0; i<isSelects.length; i++){
                    isSelects[i] = false;
                }
                thisActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 当数据改变时调用此方法通知view更新
                        setData();
                        appointmentListAdapter.notifyDataSetChanged();
                    }
                });
            } catch (JSONException e) {

            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        CheckBox cb = (CheckBox)arg1.findViewById(R.id.listview_appointment_cb_isChoose);
        cb.toggle();
        chooseId = appointmenList.get(arg2).userId;
        // 调整选定条目
        if (cb.isChecked()) {
            checkNum++;
            totalMoney = totalMoney + appointmenList.get(arg2).totalPrice;
        } else {
            checkNum--;
            totalMoney = totalMoney - appointmenList.get(arg2).totalPrice;
        }

        Message message = Message.obtain();
        message.what = SET_APPOINTMENT_USER;
        handler.sendMessage(message);

    }
}
