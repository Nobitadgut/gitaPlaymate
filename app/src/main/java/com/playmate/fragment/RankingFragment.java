package com.playmate.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.playmate.R;
import com.playmate.activity.LocationChooseActivity;
import com.playmate.activity.TaskDetailActivity;
import com.playmate.activity.SearchResultActivity;
import com.playmate.data.TaskListBean;
import com.playmate.util.HttpUtil;
import com.playmate.util.adapter.MyPagerAdapter;
import com.playmate.util.view.LoadMoreListView;
import com.playmate.util.adapter.TaskListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RankingFragment extends ListFragment {

    private static final String TAG = "RankingFragment";
    private List<TaskListBean> data;
    private ArrayList<ImageView> imageViewlist;
    private ArrayList<String> bannerPngPaths;
    private ArrayList<String> urlList;
    private String taskJsonStr;
    private Thread currentThread;
    private Thread adBannerThread;
    private TaskListViewAdapter adapter;
    //TODO searchbox and location choose
    private EditText searchBox;
    private TextView location_text;
    private LinearLayout location_choose;
    //TODO sort listview
    private RadioGroup sort_radioGroup;
    private RadioButton sort_radioButton_hot;
    private RadioButton sort_radioButton_price;
    private RadioButton sort_radioButton_distance;

    private ViewPager mViewPager;
    private LinearLayout layoutPGroup;
    private int previousPoint;
    private boolean isStop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isStop = false;

        Bundle bundle = this.getArguments();
        taskJsonStr = bundle.getString("tasklist");
        bannerPngPaths = bundle.getStringArrayList("bannerPngPath");

        initData();
        adapter = new TaskListViewAdapter(getActivity(), data);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_task, container, false);

        Log.d(TAG, "onCreateView");
        searchBox = (EditText) view.findViewById(R.id.ranking_serchBox);

        location_text = (TextView) view.findViewById(R.id.fragment_top_location_text);
        location_choose = (LinearLayout) view.findViewById(R.id.fragment_top_location_LinearLayout);

        sort_radioGroup = (RadioGroup) view.findViewById(R.id.fragment_sort_rg);
        sort_radioButton_hot = (RadioButton) view.findViewById(R.id.fragment_sort_hot);
        sort_radioButton_price = (RadioButton) view.findViewById(R.id.fragment_sort_price);

        mViewPager = (ViewPager) view.findViewById(R.id.fragment_adbanner_viewpager);
        layoutPGroup = (LinearLayout) view.findViewById(R.id.fragment_adbanner_show_pointer);

        initBanner(imageViewlist, urlList);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated");

        adBannerThread = new AdBannerThread();
        adBannerThread.start();

        setListener();
    }

    @Override
    public void onDestroyView(){
        isStop = true;
        adBannerThread.interrupt();
        super.onDestroyView();
    }

    private void initBanner(ArrayList<ImageView> imgList, ArrayList<String> urllist){
        for (int i=0; i<imgList.size(); i++){
            View v = new View(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            params.leftMargin = 12;
            v.setLayoutParams(params);
            v.setEnabled(false);
            v.setBackgroundResource(R.drawable.fragment_ranking_adbanner_pointer_selector);
            layoutPGroup.addView(v);
        }

        int index = Integer.MAX_VALUE / 2 - 3;
        mViewPager.setAdapter(new MyPagerAdapter(getActivity(), imgList, urllist));
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setCurrentItem(index);
    }

    private void initData(){
        initBannerData();
        initTaskData();
    }

    //change
    private void initBannerData() {
        data = new ArrayList<TaskListBean>();
        imageViewlist = new ArrayList<ImageView>();
        urlList = new ArrayList<String>();
        //TODO 找出原因为什么两个图片就会死，接口加入url字段
        urlList.add("http://www.baidu.com/");
        urlList.add("http://www.baidu.com/");
        urlList.add("http://www.baidu.com/");
        urlList.add("http://www.baidu.com/");

        ImageView bannerpng = new ImageView(getActivity());
        bannerpng.setImageResource(R.drawable.tu4);

        for (int i=0; i<4; i++){
            imageViewlist.add(bannerpng);
        }

//        bannerPngPaths.add(Environment.getExternalStorageDirectory().getPath() + File.separator + "playmate" + File.separator + "tu4.png");
//        bannerPngPaths.add(Environment.getExternalStorageDirectory().getPath() + File.separator + "playmate" + File.separator + "tu4.png");

//        for (String path : bannerPngPaths){
//            File imageFile = new File(path);
//            ImageView imageView = new ImageView(getActivity());
//            if (imageFile.exists()){
//                Bitmap bm = BitmapFactory.decodeFile(path);
//                imageView.setImageBitmap(bm);
//                imageViewlist.add(imageView);
//            }
//        }

//        appendData2();
    }

    private void initTaskData(){
        try {
            JSONArray taskArray = new JSONArray(taskJsonStr);
            for (int i=0; i<taskArray.length(); i++){
                JSONObject jsonObject = taskArray.getJSONObject(i);
                TaskListBean taskListBean = new TaskListBean(jsonObject);
                data.add(taskListBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void appendData(){
        String taskJsonStr = HttpUtil.getHttpData("http://120.25.56.82:9100/task/listTask");
        try {
            JSONArray taskArray = new JSONArray(taskJsonStr);
            for (int i=0; i<taskArray.length(); i++){
                JSONObject jsonObject = taskArray.getJSONObject(i);
                TaskListBean taskListBean = new TaskListBean(jsonObject);
                data.add(taskListBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setListener(){
        ((LoadMoreListView) getListView()).setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentThread = new DataLoadThread();
                currentThread.start();
            }
        });

        location_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LocationChooseActivity.class);
                startActivity(intent);
            }
        });

        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("taskId", data.get(position).task_id);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    //adbanner auto change
    class AdBannerThread extends Thread {
        @Override
        public void run() {
            while (!isStop) {
                try {
                    Thread.sleep(3000);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(mViewPager
                                    .getCurrentItem() + 1);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // 模拟加载数据
    class DataLoadThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                appendData();
//                appendData2();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 当数据改变时调用此方法通知view更新
                        adapter.notifyDataSetChanged();
                        ((LoadMoreListView) getListView()).onLoadMoreComplete();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageSelected(int position) {
            position = position % imageViewlist.size();
            layoutPGroup.getChildAt(previousPoint).setEnabled(false);
            layoutPGroup.getChildAt(position).setEnabled(true);
            previousPoint = position;
        }
    }

}
