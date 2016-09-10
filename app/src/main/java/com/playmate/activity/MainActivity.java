package com.playmate.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.playmate.R;
import com.playmate.fragment.FindFragment;
import com.playmate.fragment.MeFragment;
import com.playmate.fragment.MessageFragment;
import com.playmate.fragment.RankingFragment;
import com.playmate.util.view.MainNavigateTabBar;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;
import com.tencent.android.tpush.service.XGPushService;

public class MainActivity extends Activity {

    private Activity thisActivity;

    private static final String TAG_PAGE_HOME = "首页";
    private static final String TAG_PAGE_FIND = "发现";
    private static final String TAG_PAGE_PUBLISH = "发布";
    private static final String TAG_PAGE_MESSAGE = "消息";
    private static final String TAG_PAGE_PERSON = "我的";

    private MainNavigateTabBar mNavigateTabBar;
    private ImageButton publish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initXGPush();

        thisActivity = this;

        mNavigateTabBar = (MainNavigateTabBar) findViewById(R.id.mainTabBar);
        publish = (ImageButton) findViewById(R.id.ib_public);
        publish.setOnClickListener(new publishListener());

        Bundle bundle = this.getIntent().getBundleExtra("RankingFragment");

        mNavigateTabBar.onRestoreInstanceState(savedInstanceState);

        mNavigateTabBar.addTab(RankingFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.tab_home1, R.mipmap.tab_home2, TAG_PAGE_HOME));
        mNavigateTabBar.addTab(FindFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.tab_find1, R.mipmap.tab_find2, TAG_PAGE_FIND));
        mNavigateTabBar.addTab(null, new MainNavigateTabBar.TabParam(0, 0, TAG_PAGE_PUBLISH));
        mNavigateTabBar.addTab(MessageFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.tab_msg1, R.mipmap.tab_msg2, TAG_PAGE_MESSAGE));
        mNavigateTabBar.addTab(MeFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.tab_me1, R.mipmap.tab_me2, TAG_PAGE_PERSON));

        mNavigateTabBar.addBundle(bundle);
    }

    /**初始化信鸽推送*/
    private void initXGPush(){
        Context context = getApplicationContext();
        XGPushManager.registerPush(context);
        XGPushManager.registerPush(getApplicationContext(),
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        Log.w(Constants.LogTag,
                                "+++ register push sucess. token:" + data);

                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.w(Constants.LogTag,
                                "+++ register push fail. token:" + data
                                        + ", errCode:" + errCode + ",msg:"
                                        + msg);
                    }
                });
        Intent service = new Intent(context, XGPushService.class);
        context.startService(service);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigateTabBar.onSaveInstanceState(outState);
    }

    class publishListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(thisActivity, PublishTaskActivity.class);
            startActivity(intent);
        }
    }
}
