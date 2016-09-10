package com.playmate.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.playmate.R;
import com.playmate.util.HttpUtil;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class SplashActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGHT = 5000;
    private String taskJson;
    private ArrayList<String> bannerPngUrls;
    private ArrayList<String> bannerPngPath;
    private SharedPreferences sharedPreferences;
    private boolean isFirstLogin;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initApp();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this,
                        MainActivity.class);
                Bundle rankingFragmentBundle = new Bundle();
                rankingFragmentBundle.putString("tasklist", taskJson);
                rankingFragmentBundle.putStringArrayList("bannerPngPath", bannerPngPath);
                mainIntent.putExtra("RankingFragment", rankingFragmentBundle);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }

        }, SPLASH_DISPLAY_LENGHT);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", 11111111);
        editor.commit();
//        isFirstLogin = sharedPreferences.getBoolean("isFirstLogin", true);
//        token = sharedPreferences.getString("token", "");

        if (isFirstLogin){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else {
            //connect internet
        }
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        Log.d("TPush", "onResumeXGPushClickedResult:" + click);
        if (click != null) { // 判断是否来自信鸽的打开方式
            Toast.makeText(this, "来自信鸽点击" + click.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void initApp(){
        bannerPngUrls = new ArrayList<String>();
        bannerPngPath = new ArrayList<String>();
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "playmate");
        if (!dir.exists()){
            dir.mkdir();
        }else if (!dir.isDirectory()){
            dir.renameTo(new File(dir.getPath() + ".bak"));
            dir.mkdir();
        }
        new initThread().start();
    }

    class getBannerPngThread extends Thread {
        private String url;

        public getBannerPngThread(String s){
            this.url = s;
        }

        @Override
        public void run() {
            try {
                InputStream is = HttpUtil.getImageStream(url);
                int a = url.lastIndexOf('/');
                String fileName = url.substring(a + 1);
                File f = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "playmate" + File.separator + fileName);
                if (f.exists()){
                    bannerPngPath.add(f.getAbsolutePath());
                    return;
                }
                FileOutputStream outStream = new FileOutputStream(f);
                byte[] buffer = new byte[1024];
                int len;
                while( (len = is.read(buffer)) != -1 ){
                    outStream.write(buffer, 0, len);
                }
                outStream.close();
                is.close();
                bannerPngPath.add(f.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class initThread extends Thread{
        @Override
        public void run(){
            String bannerUrl = "http://120.25.56.82:9100/banner/listBanner?advType=banner&page=main&advPosition=top";
            String bannerJsonStr = HttpUtil.getHttpData(bannerUrl);
            if (!bannerJsonStr.equals("")){
                try {
                    JSONArray bannerJsonArray = new JSONArray(bannerJsonStr);
                    for (int i=0; i<bannerJsonArray.length(); i++){
                        JSONObject jo = bannerJsonArray.getJSONObject(i);
                        bannerPngUrls.add(jo.getString("url"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (String url : bannerPngUrls){
                getBannerPngThread thread = new getBannerPngThread(url);
                thread.start();
            }

            String taskJsonStr = HttpUtil.getHttpData("http://120.25.56.82:9100/task/listTask");
            if (!taskJsonStr.equals("")){
                taskJson = taskJsonStr;
            }else {
                taskJson = "";
            }
        }
    }
}