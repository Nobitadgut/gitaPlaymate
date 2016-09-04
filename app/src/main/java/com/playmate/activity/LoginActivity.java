package com.playmate.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.playmate.R;
import com.playmate.util.CustomProgress;
import com.playmate.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    private Activity thisActivity;
    private Button loginButton;
    private Button registerButton;
    private EditText accountEditText;
    private EditText passwordEditText;
    private ImageButton delete;
    private CheckBox pwIsVisible;
    private ImageButton back;
    private TextView forgetPw;
    private CustomProgress progressDialog;
    private SharedPreferences sharedPreferences;

    private String loginMessage;

    private static final int LOGIN_SUCCEED = 1;
    private static final int LOGIN_FAILD = 2;

    private Handler handler = new Handler() {

        // 处理子线程给我们发送的消息。
        @Override
        public void handleMessage(android.os.Message msg) {

            if (msg.what == LOGIN_SUCCEED){
                thisActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
//                        Intent intent = new Intent(thisActivity, CompleteDataActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putLong("userId", userId);
//                        bundle.putString("userLoginPhone", userLoginPhone);
//                        intent.putExtra("bundle", bundle);
//                        startActivity(intent);
                        finish();
                    }
                });
            }else if (msg.what == LOGIN_FAILD){
                thisActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loginMessage.equals("")) loginMessage = "登陆错误";
                        Toast.makeText(thisActivity, loginMessage, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        thisActivity = this;
        loginMessage = "";

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        initView();
        setListener();

    }

    private void initView(){
        back = (ImageButton) findViewById(R.id.activity_login_ib_back);
        registerButton = (Button)findViewById(R.id.activity_login_button_registerButton);
        accountEditText = (EditText)findViewById(R.id.activity_login_et_accountEditText);
        passwordEditText = (EditText)findViewById(R.id.activity_login_et_passwordEditText);
        delete = (ImageButton) findViewById(R.id.activity_login_ib_delete);
        pwIsVisible = (CheckBox) findViewById(R.id.activity_login_cb_isvisible);
        loginButton = (Button)findViewById(R.id.activity_login_button_loginButton);
        forgetPw = (TextView) findViewById(R.id.activity_login_tv_forgetpw);
    }

    private void setListener(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountEditText.setText("");
            }
        });

        pwIsVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (pwIsVisible.isChecked()) passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                else passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(thisActivity, "忘记密码", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void login(){

        JSONObject loginJson = new JSONObject();
        String jstr;
        try {
            loginJson.put("userLoginPhone", accountEditText.getText().toString());
            loginJson.put("userLoginPassword", passwordEditText.getText().toString());
            jstr = loginJson.toString();
        } catch (JSONException e) {
            jstr = "";
        }

        LoginThread loginThread = new LoginThread(jstr);
        loginThread.start();

        progressDialog = CustomProgress.show(this, "登录中...", true, null);
    }

    class LoginThread extends Thread{
        private String jstr;

        public LoginThread(String jstr){
            this.jstr = jstr;
        }

        @Override
        public void run() {
            String resultDate = HttpUtil.getHttpData("http://120.25.56.82:9100/user/login", jstr);
            try {
                //TODO create login bean
                JSONObject resultJo = new JSONObject(resultDate);
                String code = resultJo.getString("code");
                String data = resultJo.getString("data");
                if (!data.equals("")){
                    JSONObject userJson = new JSONObject(data);
                }
                loginMessage = resultJo.getString("message");
                if (code.equals("1")){
                    Message message = Message.obtain();
                    message.what = LOGIN_SUCCEED;
                    handler.sendMessage(message);
                }else {
                    Message message = Message.obtain();
                    message.what = LOGIN_FAILD;
                    handler.sendMessage(message);
                }
            } catch (JSONException e) {
                Message message = Message.obtain();
                message.what = LOGIN_FAILD;
                handler.sendMessage(message);
            }
        }
    }
}
