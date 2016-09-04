package com.playmate.activity;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import com.playmate.R;
import com.playmate.util.CustomProgress;
import com.playmate.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends Activity {

    private Activity thisActivity;
    private ImageButton back;
    private EditText phoneNumber;
    private EditText passWord;
    private EditText checkCode;
    private ImageButton delete;
    private CheckBox pwIsVisible;
    private Button getCheckCode;
    private Button nextStep;
    private CustomProgress progressDialog;

    private long userId;
    private String userLoginPhone;
    private String registerMessage;

    private static final int REGISTER_SUCCEED = 1;
    private static final int REGISTER_FAILD = 2;

    private Handler handler = new Handler() {

        // 处理子线程给我们发送的消息。
        @Override
        public void handleMessage(android.os.Message msg) {

            if (msg.what == REGISTER_SUCCEED){
                thisActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Intent intent = new Intent(thisActivity, CompleteDataActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putLong("userId", userId);
                        bundle.putString("userLoginPhone", userLoginPhone);
                        intent.putExtra("bundle", bundle);
                        startActivity(intent);
                        finish();
                    }
                });
            }else if (msg.what == REGISTER_FAILD){
                thisActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (registerMessage.equals("")) registerMessage = "注册错误";
                        Toast.makeText(thisActivity, registerMessage, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        thisActivity = this;
        userId = 0;
        userLoginPhone = "";
        registerMessage = "";

        initView();
        setListener();
    }

    private void register(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("verifyCode", checkCode.getText().toString());
            jo.put("userLoginPhone", phoneNumber.getText().toString());
            jo.put("userLoginPassword", passWord.getText().toString());
        } catch (JSONException e) {
            Toast.makeText(thisActivity, "错误，请检测网络", Toast.LENGTH_LONG).show();
        }

        RegisterThread registerThread = new RegisterThread(jo.toString());
        registerThread.start();

        progressDialog = CustomProgress.show(this, "注册中...", true, null);
    }

    private void initView(){
        back = (ImageButton)findViewById(R.id.activity_checkphone_ib_back);
        phoneNumber = (EditText)findViewById(R.id.activity_checkphone_et_phoneEditText);
        passWord = (EditText) findViewById(R.id.activity_checkphone_et_passwordEditText);
        checkCode = (EditText)findViewById(R.id.activity_checkphone_et_checkCodeEditText);
        delete = (ImageButton)findViewById(R.id.activity_checkphone_ib_delete);
        pwIsVisible = (CheckBox)findViewById(R.id.activity_checkphone_cb_isvisible);
        getCheckCode = (Button)findViewById(R.id.activity_checkphone_button_getCheckCode);
        nextStep = (Button)findViewById(R.id.activity_checkphone_button_nextStep);

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
                phoneNumber.setText("");
            }
        });

        pwIsVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (pwIsVisible.isChecked()) passWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                else passWord.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        getCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(thisActivity, "123456", Toast.LENGTH_SHORT).show();
            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
//                Intent intent = new Intent(thisActivity, CompleteDataActivity.class);
//                startActivity(intent);
            }
        });
    }

    class RegisterThread extends Thread{
        private String jstr;

        public RegisterThread(String jstr){
            this.jstr = jstr;
        }

        @Override
        public void run() {
            String resultDate = HttpUtil.getHttpData("http://120.25.56.82:9100/user/register", jstr);
            try {
                //TODO create login bean
                JSONObject resultJo = new JSONObject(resultDate);
                String code = resultJo.getString("code");
                String data = resultJo.getString("data");
                if (!data.equals("")){
                    JSONObject userJson = new JSONObject(data);
                    userId = userJson.getLong("userId");
                    userLoginPhone = userJson.getString("userLoginPhone");
                }
                registerMessage = resultJo.getString("message");
                if (code.equals("1")){
                    Message message = Message.obtain();
                    message.what = REGISTER_SUCCEED;
                    handler.sendMessage(message);
                }else {
                    Message message = Message.obtain();
                    message.what = REGISTER_FAILD;
                    handler.sendMessage(message);
                }
            } catch (JSONException e) {
                Message message = Message.obtain();
                message.what = REGISTER_FAILD;
                handler.sendMessage(message);
            }
        }
    }
}
