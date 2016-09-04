package com.playmate.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nnn7h on 16-7-19.
 */
public class CommentBean {
    //user data
    public String userHeaderUrl;
    public long userId;
    public String user_name;
    //task
    public String taskId;
    //comment context
    public String commentContext;
    public String sendTimeString;

    public CommentBean(JSONObject taskjson) throws JSONException {
        this.userId = taskjson.getLong("userId");
        this.userHeaderUrl = taskjson.getString("userHeaderUrl");
        this.user_name = taskjson.getString("userName");
        this.taskId = taskjson.getString("taskId");
        this.commentContext = taskjson.getString("context");
        this.sendTimeString = "15分钟前";
    }
}
