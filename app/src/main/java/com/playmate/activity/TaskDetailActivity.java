package com.playmate.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.playmate.R;
import com.playmate.data.CommentBean;
import com.playmate.data.TaskDetailBean;
import com.playmate.util.util;
import com.playmate.util.HttpUtil;
import com.playmate.util.adapter.CommentListViewAdapter;
import com.playmate.util.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TaskDetailActivity extends Activity {
    private static final String TAG = "TaskDetailActivity";

    private long selfUserId;

    private String dataJsonStr;
    private Activity thisActivity;
    private TaskDetailBean taskDetailBean;
    private List<CommentBean> commentList;

    private LinearLayout activity_detail_ll_loading;
    private ScrollView activity_detail_sv;

    private ImageButton ib_back;
    private ImageButton ib_keep;
    private ImageButton ib_share;
    private TextView actionbar_tv_missionTitle;

    private CircleImageView iv_userIcon;
    private TextView tv_userName;
    private LinearLayout ll_sexul;
    private TextView tv_age;
    private TextView tv_creditScore;
    private LinearLayout ll_isPass_pass;
    private LinearLayout ll_isPass_unpass;
    private LinearLayout ll_usrData;

    private TextView middle_tv_missionTitle;
    private TextView middle_tv_price;
    private TextView middle_tv_missionTime;
    private TextView middle_tv_address;
    private TextView middle_tv_distance;
    private TextView middle_tv_missiondes;

    private LinearLayout ll_sign;
    private LinearLayout ll_comment;
    private TextView tv_signNumner;
    private TextView tv_commentNumber;

    private ImageView iv_missionImage;

    private ListView lv_comment;
    private TextView tv_showAllCommnet;

    private RelativeLayout rl_report;

    private Button button_commnet;
    private Button button_reservation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mission_detail);
        thisActivity = this;
        try {
            Bundle bundle = this.getIntent().getBundleExtra("bundle");
            if (bundle == null) {
                Toast.makeText(this, "网络连接失败，请检测网络！", Toast.LENGTH_LONG).show();
                return;
            }
            String taskId = bundle.getString("taskId", "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("taskId", taskId);
            dataJsonStr = jsonObject.toString();
        } catch (Exception e) {
            dataJsonStr = "";
        }

        initView();
        initData();
        setListener();
    }

    private CommentBean testComment() {
        JSONObject jo = new JSONObject();
        CommentBean commentBean;
        try {
            jo.put("userId", 11111111);
            jo.put("userHeaderUrl", "");
            jo.put("userName", "小何姗姗");
            jo.put("taskId", "SYS11111111");
            jo.put("context", "发起这个任务的人靠谱吗?");
            commentBean = new CommentBean(jo);
        } catch (Exception e) {
            return null;
        }
        return commentBean;
    }

    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        selfUserId = sharedPreferences.getLong("userId", 0);

        if (dataJsonStr.equals("")) {
            Toast.makeText(this, "网络连接失败，请检测网络！", Toast.LENGTH_LONG).show();
            return;
        }
        commentList = new ArrayList<CommentBean>();
        for (int i = 0; i < 3; i++) {
            commentList.add(testComment());
        }
        getTaskDetailThread thread = new getTaskDetailThread(dataJsonStr);
        thread.start();
    }

    private void initView() {
        activity_detail_ll_loading = (LinearLayout) findViewById(R.id.activity_detail_ll_loading);
        activity_detail_sv = (ScrollView) findViewById(R.id.activity_detail_sv);

        ib_back = (ImageButton) findViewById(R.id.activity_detail_ib_back);
        ib_keep = (ImageButton) findViewById(R.id.activity_detail_ib_keep);
        ib_share = (ImageButton) findViewById(R.id.activity_detail_ib_share);
        actionbar_tv_missionTitle = (TextView) findViewById(R.id.activity_detail_actionbar_tv_missionTitle);

        iv_userIcon = (CircleImageView) findViewById(R.id.activity_detail_iv_userIcon);
        tv_userName = (TextView) findViewById(R.id.activity_detail_tv_userName);
        ll_sexul = (LinearLayout) findViewById(R.id.activity_detail_ll_sexul);
        tv_age = (TextView) ll_sexul.findViewById(R.id.lab_sexul_age);
        tv_creditScore = (TextView) findViewById(R.id.activity_detail_tv_creditScore);
        ll_isPass_pass = (LinearLayout) findViewById(R.id.activity_detail_ll_isPass_pass);
        ll_isPass_unpass = (LinearLayout) findViewById(R.id.activity_detail_ll_isPass_unPass);
        ll_usrData = (LinearLayout) findViewById(R.id.activity_detail_ll_usrData);

        middle_tv_missionTitle = (TextView) findViewById(R.id.activity_detail_middle_tv_misssionTitle);
        middle_tv_price = (TextView) findViewById(R.id.activity_detail_middle_tv_price);
        middle_tv_missionTime = (TextView) findViewById(R.id.activity_detail_middle_tv_missionTime);
        middle_tv_address = (TextView) findViewById(R.id.activity_detail_middle_tv_address);
        middle_tv_distance = (TextView) findViewById(R.id.activity_detail_middle_tv_distance);
        middle_tv_missiondes = (TextView) findViewById(R.id.activity_detail_middle_tv_missiondes);

        ll_sign = (LinearLayout) findViewById(R.id.activity_detail_ll_sign);
        tv_signNumner = (TextView) findViewById(R.id.activity_detail_tv_signNumner);
        ll_comment = (LinearLayout) findViewById(R.id.activity_detail_ll_comment);
        tv_commentNumber = (TextView) findViewById(R.id.activity_detail_tv_commentNumner);

        iv_missionImage = (ImageView) findViewById(R.id.activity_detail_iv_missionImage);

        lv_comment = (ListView) findViewById(R.id.activity_detail_lv_comment);
        tv_showAllCommnet = (TextView) findViewById(R.id.activity_detail_tv_showAllcomment);

        rl_report = (RelativeLayout) findViewById(R.id.activity_detail_rl_report);

        button_commnet = (Button) findViewById(R.id.activity_detail_button_commnet);
        button_reservation = (Button) findViewById(R.id.activity_detail_button_reservation);
    }

    private void setListener() {
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ib_keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(thisActivity, "keep", Toast.LENGTH_LONG).show();
            }
        });

        ib_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(thisActivity, "share", Toast.LENGTH_LONG).show();
            }
        });

        ll_usrData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(thisActivity, "user_data", Toast.LENGTH_LONG).show();
            }
        });

//        ll_sign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(thisActivity, "sign", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        ll_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(thisActivity, "comment", Toast.LENGTH_LONG).show();
//            }
//        });

        tv_showAllCommnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(thisActivity, "显示所有评论", Toast.LENGTH_LONG).show();
            }
        });

        rl_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(thisActivity, "举报", Toast.LENGTH_LONG).show();
            }
        });

        button_commnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(thisActivity, "留言", Toast.LENGTH_LONG).show();
            }
        });

        button_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selfUserId == taskDetailBean.userId){
                    Intent intent = new Intent(thisActivity, AppointmentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("taskId", taskDetailBean.task_id);
                    bundle.putString("eventPrice", taskDetailBean.event_price);
                    bundle.putString("eventTime", taskDetailBean.event_time);
                    bundle.putInt("singlePrice", taskDetailBean.single_price);
                    bundle.putString("adress", taskDetailBean.adress);
                    bundle.putInt("timeLen", taskDetailBean.time_length);
                    bundle.putLong("userId", selfUserId);

                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                }else {
                    Toast.makeText(thisActivity, "报名", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(thisActivity, SignUpPopupWindow.class);
//                    startActivity(intent);

                    SignUpPopupWindow menuWindow = new SignUpPopupWindow(thisActivity);
                    menuWindow.showAtLocation(thisActivity.findViewById(R.id.activity_detail_ll_parent), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });
    }

    private void setData() {
        if (taskDetailBean == null) {
            return;
        }
        actionbar_tv_missionTitle.setText(taskDetailBean.task_title);

        iv_userIcon.setImageResource(R.drawable.tu3);
        tv_userName.setText(taskDetailBean.user_name);
        if (taskDetailBean.isPass) {
            ll_isPass_pass.setVisibility(View.VISIBLE);
        } else {
            ll_isPass_unpass.setVisibility(View.VISIBLE);
        }

        if (taskDetailBean.user_gender == 1) {
            ll_sexul.setBackgroundResource(R.mipmap.sex1);
        } else {
            ll_sexul.setBackgroundResource(R.mipmap.sex2);
        }
        tv_age.setText(taskDetailBean.age);
        tv_creditScore.setText(taskDetailBean.credit_Str);

        middle_tv_missionTitle.setText(taskDetailBean.task_title);
        middle_tv_price.setText(taskDetailBean.event_price);
        middle_tv_missionTime.setText(taskDetailBean.event_time);
        middle_tv_address.setText(taskDetailBean.adress);
        middle_tv_distance.setText(taskDetailBean.destince);
        middle_tv_missiondes.setText("周末邀请一齐看电影，可以在天河城附近的有空小伙伴.");
//        middle_tv_missiondes.setText(taskDetailBean.task_des);

        Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + File.separator + "playmate" + File.separator + "tu6.png");
        iv_missionImage.setImageBitmap(bm);

        CommentListViewAdapter commAdapter = new CommentListViewAdapter(thisActivity, commentList);
        lv_comment.setAdapter(commAdapter);
        util.setListViewHeightBasedOnChildren(lv_comment);

        if (selfUserId == taskDetailBean.userId)
            button_reservation.setText("已预约3人");
        else
            button_reservation.setText("去报名");
    }

    class getTaskDetailThread extends Thread {
        String mTaskId;

        public getTaskDetailThread(String taskid) {
            mTaskId = taskid;
        }

        @Override
        public void run() {
            String resultDate = HttpUtil.getHttpData("http://120.25.56.82:9100/task/getTaskDetail", dataJsonStr);
            try {
                JSONObject resultJson = new JSONObject(resultDate);
                taskDetailBean = new TaskDetailBean(resultJson);
                thisActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setData();
                        activity_detail_ll_loading.setVisibility(View.GONE);
                        activity_detail_sv.setVisibility(View.VISIBLE);
                    }
                });
                Thread.sleep(1000);
            } catch (JSONException e) {
                taskDetailBean = null;
            } catch (InterruptedException e) {

            }
        }
    }
}
