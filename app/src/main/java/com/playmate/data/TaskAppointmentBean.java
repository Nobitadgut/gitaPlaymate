package com.playmate.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * [{"creditScore":56,
 * "isChosen":0,
 * "isPass":0,
 * "singlePrice":1,
 * "totalPrice":0,
 * "userBirthday":519577200000,
 * "userEmail":"system@playmate.com",
 * "userGender":1,
 * "userHeaderUrl":"http://img3.duitang.com/uploads/item/201504/13/20150413H5548_BuNcZ.thumb.700_0.jpeg",
 * "userId":11111111,
 * "userLoginPhone":"18800880088",
 * "userName":"系统",
 * "age":30}]
 */
public class TaskAppointmentBean {

    public long userId;
    public String userName;
    public String userHeaderUrl;
    public int userGender;
    public long userBirthday;
    public int age;
    public String ageStr;
    public String userLoginPhone;
    public String userEmail;
    public int creditScore;
    public int totalPrice;
    public String totalPriceStr;
    public int singlePrice;
    public String singlePriceStr;
    public int isPass;
    public int isChosen;
    public String credit_Str;

    public TaskAppointmentBean(JSONObject taskjson) throws JSONException {
        this.userId = taskjson.getLong("userId");
        this.userName = taskjson.getString("userName");
//        this.userHeaderUrl = taskjson.getString("userHeaderUrl");
        this.userHeaderUrl = "";
        this.userGender = taskjson.getInt("userGender");
        this.userBirthday = taskjson.getLong("userBirthday");
        this.age = taskjson.getInt("age");
        this.userLoginPhone = taskjson.getString("userLoginPhone");
        this.userEmail = taskjson.getString("userEmail");
        this.creditScore = taskjson.getInt("creditScore");
        try {
            this.totalPrice = taskjson.getInt("totalPrice");
        }catch (Exception e){
            this.totalPrice = 0;
        }

        this.singlePrice = taskjson.getInt("singlePrice");
        try {
            this.isChosen = taskjson.getInt("isChosen");
        }catch (Exception e){
            this.isChosen = 0;
        }
        try {
            this.isPass = taskjson.getInt("isPass");
        }catch (Exception e){
            this.isPass = 0;
        }
//        this.isPass = taskjson.getInt("isPass");
//        this.isChosen = taskjson.getInt("isChosen");
        this.ageStr = "" + this.age;
        this.credit_Str = "信用" + this.creditScore;
        this.singlePriceStr = "" + this.singlePrice;
        this.totalPriceStr = "" + this.totalPrice;

    }
}
