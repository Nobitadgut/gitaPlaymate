package com.playmate.data;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskListBean {
    //adress
    public String adress;
    public int area;
    public int city;
    public int province;
    public String destince;
    //time
    private long CREATE_TIME;
    private long END_TIME;
    private long START_TIME;
    private long UPDATE_TIME;
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
    //hot count
    public int admire_count;
    public int browse_count;
    public int share_count;
    //user data
    public String userHeaderUrl;
    public long userId;
    public String user_name;
    public int user_gender;
    public int credit_score;
    public String credit_Str;
    public String age;
    private long user_birthday;
    private String user_email;
    private String user_login_phone;

    public TaskListBean(JSONObject taskjson) throws JSONException{
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        this.adress = taskjson.getString("address");
        this.area = taskjson.getInt("area");
        this.city = taskjson.getInt("city");
        this.province = taskjson.getInt("province");
        this.destince = 3 + "km";

        this.CREATE_TIME = taskjson.getLong("createTime");
        this.END_TIME = taskjson.getLong("endTime");
        this.START_TIME = taskjson.getLong("startTime");
        this.UPDATE_TIME = taskjson.getLong("updateTime");
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
        if (taskjson.getString("timeType").equals("H")){
            this.time_type = "小时";
        }else {
            this.time_type = "分钟";
        }
        this.event_price = "¥" + single_price + "/" +time_length + time_type;


//        this.admire_count = taskjson.getInt("admire_count");
//        this.browse_count = taskjson.getInt("browse_count");
//        this.share_count = taskjson.getInt("share_count");

//        this.userId = taskjson.getInt("userId");
//        this.userHeaderUrl = taskjson.getString("userHeaderUrl");
//        this.user_name = taskjson.getString("userName");
//        this.user_gender = taskjson.getInt("userGender");
//        this.user_birthday = taskjson.getLong("userBirthday");
//        this.credit_score = taskjson.getInt("creditScore");
//        this.user_email = taskjson.getString("userEmail");
//        this.user_login_phone = taskjson.getString("userLoginPhone");
//        this.age = "" + taskjson.getInt("age");

        try {
            this.admire_count = taskjson.getInt("admireCount");
            this.browse_count = taskjson.getInt("browseCount");
            this.share_count = taskjson.getInt("shareCount");
        }catch (JSONException e){
            this.admire_count = 0;
            this.browse_count = 0;
            this.share_count = 0;
        }

        try {
            this.userId = taskjson.getLong("userId");
        }catch (JSONException e){
            this.userId = 0;
        }
        try {
            this.userHeaderUrl = taskjson.getString("userHeaderUrl");
        }catch (JSONException e){
            this.userHeaderUrl = "";
        }
        try{
            this.user_name = taskjson.getString("userName");
        }catch (JSONException e){
            this.user_name = "cuowu";
        }
        try {
            this.user_gender = taskjson.getInt("userGender");
        }catch (JSONException e){
            this.user_gender = 1;
        }
        try {
            this.user_birthday = taskjson.getLong("userBirthday");
        }catch (JSONException e){
            this.user_birthday = 0;
        }
        try {
            this.credit_score = taskjson.getInt("creditScore");
        }catch (JSONException e){
            this.credit_score = 60;
        }
        try {
            this.user_email = taskjson.getString("userEmail");
        }catch (JSONException e){
            this.user_email = "";
        }
        try {
            this.user_login_phone = taskjson.getString("userLoginPhone");
        }catch (JSONException e){
            this.user_login_phone = "";
        }
        try {
            this.age = "" + taskjson.getInt("age");
        }catch (JSONException e){
            this.age = "" + 26;
        }

        this.credit_Str = "信用" + credit_score;
    }
}
