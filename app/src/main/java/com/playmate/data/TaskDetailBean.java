package com.playmate.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskDetailBean {
    //adress
    public String adress;
    public int area;
    public int city;
    public int province;
    public String destince;
    //time
    private long END_TIME;
    private long START_TIME;
    public String day;
    public String start_time;
    public String end_time;
    public String event_time;
    //task
    public String task_id;
    public int single_price;
    public int task_status;
    public String task_title;
    public String task_type;
    public int time_length;
    public String time_type;
    public String event_price;
    public String task_des;
    //unknown what
    public String note;
    //user data
    public String userHeaderUrl;
    public long userId;
    public String user_name;
    public int user_gender;
    public int credit_score;
    public String credit_Str;
    public String age;
    public boolean isPass;
    private long user_birthday;
    private String user_email;
    private String user_login_phone;

    public TaskDetailBean(JSONObject taskjson) throws JSONException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        this.adress = taskjson.getString("address");
        this.area = taskjson.getInt("area");
        this.city = taskjson.getInt("city");
        this.province = taskjson.getInt("province");
        this.destince = 3 + "km";

        this.END_TIME = taskjson.getLong("endTime");
        this.START_TIME = taskjson.getLong("startTime");
        date.setTime(START_TIME);
        this.day = sdf.format(date);
        sdf = new SimpleDateFormat("HH:mm");
        this.start_time = sdf.format(date);
        date.setTime(END_TIME);
        this.end_time = sdf.format(date);
        this.event_time = day +  " " + start_time + "-" + end_time;

        this.single_price = taskjson.getInt("singlePrice");
        this.task_status = taskjson.getInt("taskStatus");
        this.task_title = taskjson.getString("taskTitle");
        this.time_length = taskjson.getInt("timeLength");
        this.task_type = taskjson.getString("taskType");
        this.task_id = taskjson.getString("taskId");
        this.task_des = taskjson.getString("taskDesc");
        if (taskjson.getString("timeType").equals("H")){
            this.time_type = "小时";
        }else {
            this.time_type = "分钟";
        }
        this.event_price = "¥" + single_price + "/" +time_length + time_type;

        this.note = taskjson.getString("note");

        this.userId = taskjson.getLong("userId");
        this.userHeaderUrl = taskjson.getString("userHeaderUrl");
        this.user_name = taskjson.getString("userName");
        this.user_gender = taskjson.getInt("userGender");
        this.user_birthday = taskjson.getLong("userBirthday");
        this.credit_score = taskjson.getInt("creditScore");
        this.credit_Str = "信用" + credit_score;
        this.user_email = taskjson.getString("userEmail");
        this.user_login_phone = taskjson.getString("userLoginPhone");
//        this.age = "" + taskjson.getInt("age");
//        this.isPass = taskjson.getBoolean("ispass");
        this.isPass = true;
        this.age = "26";
    }
}
