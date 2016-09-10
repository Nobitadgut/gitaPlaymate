package com.playmate.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.playmate.R;
import com.playmate.util.view.CircleImageView;

public class PayActivity extends Activity {
    private Activity thisActivity;
    private long chooseId;
    private String eventTime;
    private String eventPrice;
    private int singlePrice;
    private String adress;
    private int timeLen;

    private ImageButton ib_back;
    private TextView tv_orderNumber;
    private TextView tv_taskTime;
    private TextView tv_taskAdress;
    private TextView tv_taskSinglePrice;
    private TextView tv_timelen;
    private TextView tv_taskTotalPrice;

    private CircleImageView iv_userIcon;
    private TextView tv_userName;
    private LinearLayout ll_sexul;
    private TextView tv_age;
    private TextView tv_userPrice;
    private TextView tv_userRealName;
    private LinearLayout ll_isPass_pass;
    private LinearLayout ll_isPass_unpass;

    private CheckBox cb_isRegBag;
    private TextView tv_redbag;
    private TextView tv_payMoney;
    private int redbagMoney;
    private int totalMoney;

    private CheckBox cb_alipay;
    private CheckBox cb_mmpay;

    private TextView tv_totalPay;
    private Button button_pay;
    private int pay_flag = 3;
    private final int ALIPAY_FLAG = 1;
    private final int MMPAY_FLAG = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        thisActivity = this;
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        chooseId = bundle.getLong("chooseId", 0);
        eventPrice = bundle.getString("eventPrice", "");
        eventTime = bundle.getString("eventPrice", "");
        singlePrice = bundle.getInt("singlePrice", 0);
        adress = bundle.getString("adress", "");
        timeLen = bundle.getInt("timeLen", 1);

        initData();
        initView();
        setListener();

        getDataThread thread = new getDataThread();
        thread.start();
    }

    private void initData(){
        redbagMoney = 10;
        totalMoney = 90;
    }

    private void initView(){
        ib_back = (ImageButton) findViewById(R.id.activity_pay_ib_back);
        tv_orderNumber = (TextView) findViewById(R.id.activity_pay_tv_orderNumber);
        tv_taskTime = (TextView) findViewById(R.id.activity_pay_tv_missionTime);
        tv_taskAdress = (TextView) findViewById(R.id.activity_pay_tv_missionAdress);
        tv_taskSinglePrice = (TextView) findViewById(R.id.activity_pay_tv_missionPrice);
        tv_timelen = (TextView) findViewById(R.id.activity_pay_tv_totalTime);
        tv_taskTotalPrice = (TextView) findViewById(R.id.activity_pay_tv_totalMoney);

        iv_userIcon = (CircleImageView) findViewById(R.id.activity_pay_iv_userIcon);
        tv_userName = (TextView) findViewById(R.id.activity_pay_tv_userName);
        ll_sexul = (LinearLayout) findViewById(R.id.activity_pay_ll_sexul);
        tv_age = (TextView) ll_sexul.findViewById(R.id.lab_sexul_age);
        tv_userPrice = (TextView) findViewById(R.id.activity_pay_tv_userPrice);
        tv_userRealName = (TextView) findViewById(R.id.activity_pay_tv_userRealName);
        ll_isPass_pass = (LinearLayout) findViewById(R.id.activity_pay_ll_isPass_pass);
        ll_isPass_unpass = (LinearLayout) findViewById(R.id.activity_pay_ll_isPass_unPass);

        cb_isRegBag = (CheckBox) findViewById(R.id.activity_pay_redbag_checkbox);
        tv_redbag = (TextView) findViewById(R.id.activity_pay_tv_redbagMoney);
        tv_payMoney = (TextView) findViewById(R.id.activity_pay_tv_toPayMoney);

        cb_alipay = (CheckBox) findViewById(R.id.activity_pay_cb_alipay);
        cb_mmpay = (CheckBox) findViewById(R.id.activity_pay_cb_mmpay);

        tv_totalPay = (TextView) findViewById(R.id.activity_pay_tv_finalPay);
        button_pay = (Button) findViewById(R.id.activity_pay_button_pay);
    }

    private void setListener(){
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisActivity.finish();
            }
        });

        cb_isRegBag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb_isRegBag.isChecked()){
                    totalMoney = totalMoney -redbagMoney;
                    tv_redbag.setText("-￥" + redbagMoney + ".00");
                    tv_payMoney.setText("￥" + totalMoney + ".00");
                    tv_totalPay.setText("￥" + totalMoney);
                }else {
                    totalMoney = totalMoney + redbagMoney;
                    tv_redbag.setText("-￥" + "00.00");
                    tv_payMoney.setText("￥" + totalMoney + ".00");
                    tv_totalPay.setText("￥" + totalMoney);
                }
            }
        });

        cb_alipay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb_alipay.isChecked()){
                    cb_mmpay.setChecked(false);
                    pay_flag = ALIPAY_FLAG;
                }
            }
        });

        cb_mmpay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb_mmpay.isChecked()){
                    cb_alipay.setChecked(false);
                    pay_flag = MMPAY_FLAG;
                }
            }
        });

        button_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (pay_flag){
                    case ALIPAY_FLAG:
                        Toast.makeText(thisActivity, "支付宝", Toast.LENGTH_LONG).show();
                    case MMPAY_FLAG:
                        Toast.makeText(thisActivity, "微信", Toast.LENGTH_LONG).show();
                    default:
                        Toast.makeText(thisActivity, "请选择支付方式", Toast.LENGTH_LONG).show();
                }
//                Intent intent = new Intent(thisActivity, PayActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void setData(){
        tv_orderNumber.setText("2342352352432342");
        tv_taskTime.setText(eventTime);
        tv_taskSinglePrice.setText(eventPrice);
        tv_taskAdress.setText(adress);
        tv_timelen.setText("共" + timeLen + "小时");
        tv_taskTotalPrice.setText("￥" + singlePrice * timeLen + ".00");

        iv_userIcon.setImageResource(R.drawable.tu3);
//        tv_userName.setText("");
        ll_sexul.setBackgroundResource(R.mipmap.sex1);
        tv_age.setText("" + 30);
        tv_userPrice.setText(eventPrice);
//        tv_userRealName.setText("");
        if (true) {
            ll_isPass_pass.setVisibility(View.VISIBLE);
            ll_isPass_unpass.setVisibility(View.GONE);
        } else {
            ll_isPass_pass.setVisibility(View.GONE);
            ll_isPass_unpass.setVisibility(View.VISIBLE);
        }

        tv_redbag.setText("-￥" + "00.00");
        tv_payMoney.setText("￥" + totalMoney + ".00");

        tv_totalPay.setText("￥" + totalMoney);
    }

    class getDataThread extends Thread {
        String mTaskId;

        @Override
        public void run() {
//            String resultDate = HttpUtil.getHttpData("http://120.25.56.82:9100/task/getTaskDetail", dataJsonStr);
            try {
//                JSONObject resultJson = new JSONObject(resultDate);
//                taskDetailBean = new TaskDetailBean(resultJson);
                thisActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setData();
//                        activity_detail_ll_loading.setVisibility(View.GONE);
//                        activity_detail_sv.setVisibility(View.VISIBLE);
                    }
                });
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }

}
