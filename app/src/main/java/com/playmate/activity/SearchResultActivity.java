package com.playmate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.playmate.R;
import com.playmate.data.TaskListBean;
import com.playmate.util.HttpUtil;
import com.playmate.util.adapter.TaskListViewAdapter;
import com.playmate.util.view.SearchBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends Activity implements SearchBox.SearchViewListener{

    private static final String TAG = "SearchResultActivity";
    private Activity thisActivity;
    /**
     * 搜索结果列表view
     */
    private ListView lvResults;

    /**
     * 搜索view
     */
    private SearchBox searchView;


    /**
     * 热搜框列表adapter
     */
    private ArrayAdapter<String> hintAdapter;

    /**
     * 热搜版数据
     */
    private List<String> hintData;

    /**
     * 自动补全列表adapter
     */
    private ArrayAdapter<String> autoCompleteAdapter;

    /**
     * 搜索过程中自动补全数据
     */
    private List<String> autoCompleteData;

    /**
     * 搜索结果列表adapter
     */
    private TaskListViewAdapter resultAdapter;
    /**
     * 所有数据列表，可以是数据库中数据，也可以用网络数据代替
     */
    private List<String> taskTitleData;

    /**
     * 搜索结果的数据
     */
    private List<TaskListBean> resultData;

    /**
     * 默认提示框显示项的个数
     */
    private static int DEFAULT_HINT_SIZE = 8;

    /**
     * 结果列表默认页数
     */
    private static int DEFAULT_PAGE_SIZE = 1;

    /**
     * 结果列表默认每页显示数
     */
    private static int DEFAULT_EVERY_PAGE_SIZE = 2;

    /**
     * 提示框显示项的个数
     */
    private static int hintSize = DEFAULT_HINT_SIZE;
    private static int pageSize = DEFAULT_PAGE_SIZE;
    private static int everyPageSize = DEFAULT_EVERY_PAGE_SIZE;

    /**
     * 设置提示框显示项的个数
     *
     * @param hintSize 提示框显示个数
     */
    public static void setSize(int hintSize, int pageSize, int everyPageSize) {
        SearchResultActivity.hintSize = hintSize;
        SearchResultActivity.pageSize = pageSize;
        SearchResultActivity.everyPageSize = everyPageSize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        this.thisActivity = this;
        initData();
        initViews();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        lvResults = (ListView) findViewById(R.id.activity_search_lv_results);
        searchView = (SearchBox) findViewById(R.id.activity_search_searchbox);

        //设置adapter
        searchView.setTipsHintAdapter(hintAdapter);
        searchView.setAutoCompleteAdapter(autoCompleteAdapter);
        lvResults.setAdapter(resultAdapter);

        //设置监听
        searchView.setSearchViewListener(this);
        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(SearchResultActivity.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        //从数据库获取数据
        initTaskTitleData();
        //初始化热搜版数据
        initHintData();
        //初始化自动补全数据
        initAutoCompleteData();
        //初始化搜索结果数据
        initResultData();
    }

    /**
     * 获取db 数据
     */
    private void initTaskTitleData() {
        //TODO 可获取数据库全部任务名，或从网络获取
        taskTitleData = new ArrayList<String>();
        taskTitleData.add("唱歌");
        taskTitleData.add("约人唱歌");
        taskTitleData.add("约人唱歌跳舞");
        taskTitleData.add("约人唱歌跳舞吃法");
        taskTitleData.add("约人唱歌跳舞吃饭看电影");
        taskTitleData.add("约人打球");
        taskTitleData.add("约人打球吃饭");
        taskTitleData.add("约人喝酒");
        taskTitleData.add("约人唱歌喝酒");
    }

    /**
     * 获取热搜版data 和adapter
     */
    private void initHintData() {
        //TODO 可以从数据库找到用户常用的关键字，每次搜索完都保存关键字到数据库
        hintData = new ArrayList<String>(hintSize);
        hintData.add("约人唱歌" + 1);
        hintData.add("约人跳舞" + 2);
        hintData.add("约人看电影" + 3);
        hintData.add("约人吃饭" + 4);
        hintAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hintData);
    }

    /**
     * 获取自动补全data 和adapter
     */
    private void initAutoCompleteData(){
        autoCompleteData = new ArrayList<String>(hintSize);
        autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, autoCompleteData);
    }

    private void getAutoCompleteData(String text) {
        //TODO 需要一个自动补全接口,获取全部任务名
        if (autoCompleteData.size() != 0) {
            autoCompleteData.clear();
        }
        // 根据text 获取auto data
        if (taskTitleData.size() != 0) {
            for (int i = 0, count = 0; i < taskTitleData.size() && count < hintSize; i++) {
                if (taskTitleData.get(i).contains(text.trim())) {
                    autoCompleteData.add(taskTitleData.get(i));
                    count++;
                }
            }
        }
        autoCompleteAdapter.notifyDataSetChanged();
    }

    /**
     * 获取搜索结果data和adapter
     */
    private void initResultData(){
        resultData = new ArrayList<TaskListBean>();
        resultAdapter = new TaskListViewAdapter(this, resultData);
    }

    private void getResultData(String keyword) {
        if (resultData.size() != 0) {
            resultData.clear();
        }
        GetSearchThread getSearchThread = new GetSearchThread(keyword);
        getSearchThread.start();
    }

    /**
     * 当搜索框 文本改变时 触发的回调 ,更新自动补全数据
     * @param text
     */
    @Override
    public void onRefreshAutoComplete(String text) {
        getAutoCompleteData(text);
    }

    /**
     * 点击搜索键时edit text触发的回调
     * @param keyword
     */
    @Override
    public void onSearch(String keyword) {
        System.out.println("asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfa");
        //联网查找关键字
        getResultData(keyword);
        Toast.makeText(this, "完成搜素", Toast.LENGTH_SHORT).show();
    }

    class GetSearchThread extends Thread{
        private String keyword;
        public GetSearchThread(String keyword){
            this.keyword = keyword;
        }
        @Override
        public void run(){
            JSONObject jo = new JSONObject();
            try{
                jo.put("keyword", keyword);
                jo.put("pageNo", pageSize);
                jo.put("pageSize", everyPageSize);
                String taskJsonStr = HttpUtil.getHttpData("http://120.25.56.82:9100/task/searchTask", jo.toString());
                JSONArray taskArray = new JSONArray(taskJsonStr);
                for (int i=0; i<taskArray.length(); i++){
                    JSONObject jsonObject = taskArray.getJSONObject(i);
                    TaskListBean taskListBean = new TaskListBean(jsonObject);
                    resultData.add(taskListBean);
                }
                thisActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultAdapter.notifyDataSetChanged();
                        lvResults.setVisibility(View.VISIBLE);
                    }
                });
                Thread.sleep(1000);
            }catch (InterruptedException e){
                Log.d(TAG, "user Interrupted !!");
            }
            catch (JSONException e){
                Log.d(TAG, "json error!!!");
            }
        }
    }
}
