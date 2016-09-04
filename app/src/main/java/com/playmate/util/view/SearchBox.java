package com.playmate.util.view;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.playmate.R;

/**
 * SearchText
 * 搜索编辑框
 */
public class SearchBox extends LinearLayout {
    /**
     * 上下文对象
     */
    private Context mContext;
    /**
     * 删除按钮
     */
    private ImageButton ib_searchtext_delete;
    /**
     * 输入框
     */
    private EditText et_searchtext_search;
    /**
     * 返回按钮
     */
    private ImageButton ib_searchtext_back;
    /**
     * 弹出列表
     */
    private ListView lv_searchtext_tips;

    /**
     * 提示adapter （推荐adapter）
     */
    private ArrayAdapter<String> mHintAdapter;

    /**
     * 自动补全adapter 只显示名字
     */
    private ArrayAdapter<String> mAutoCompleteAdapter;

    /**
     * 搜索回调接口
     */
    private SearchViewListener msearchViewListener;

    public SearchBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.searchbox_task, null);
        addView(view);
        initViews(view);
        setListener();
    }

    private void initViews(View view) {
        ib_searchtext_back = (ImageButton) view.findViewById(R.id.ib_searchtext_back);
        ib_searchtext_delete = (ImageButton) view.findViewById(R.id.ib_searchtext_delete);
        et_searchtext_search = (EditText) view.findViewById(R.id.et_searchtext_search);
        lv_searchtext_tips = (ListView) view.findViewById(R.id.lv_searchtext_tips);
    }

    private void setListener() {
        lv_searchtext_tips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //set edit text
                String text = lv_searchtext_tips.getAdapter().getItem(i).toString();
                et_searchtext_search.setText(text);
                et_searchtext_search.setSelection(text.length());
                //hint list view gone and result list view show
                lv_searchtext_tips.setVisibility(View.GONE);
                notifyStartSearching(text);
            }
        });

        ib_searchtext_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                et_searchtext_search.setText("");
            }
        });

        ib_searchtext_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
            }
        });

        et_searchtext_search.addTextChangedListener(new EditChangedListener());
        et_searchtext_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    lv_searchtext_tips.setVisibility(GONE);
                    notifyStartSearching(et_searchtext_search.getText().toString());
                }
                return true;
            }
        });
    }

    /**
     * 通知监听者 进行搜索操作
     *
     * @param text
     */
    private void notifyStartSearching(String text) {
        if (msearchViewListener != null) {
            msearchViewListener.onSearch(text);
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 设置搜索回调接口
     *
     * @param listener 监听者
     */
    public void setSearchViewListener(SearchViewListener listener) {
        msearchViewListener = listener;
    }

    /**
     * 设置热搜版提示 adapter
     */
    public void setTipsHintAdapter(ArrayAdapter<String> adapter) {
        this.mHintAdapter = adapter;
        if (lv_searchtext_tips.getAdapter() == null) {
            lv_searchtext_tips.setAdapter(mHintAdapter);
        }
    }

    /**
     * 设置自动补全adapter
     */
    public void setAutoCompleteAdapter(ArrayAdapter<String> adapter) {
        this.mAutoCompleteAdapter = adapter;
    }

    //文本观察者
    private class EditChangedListener implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        //当文本改变时候的操作
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!"".equals(s.toString())) {
                ib_searchtext_delete.setVisibility(VISIBLE);
                lv_searchtext_tips.setVisibility(VISIBLE);
                if (mAutoCompleteAdapter != null && lv_searchtext_tips.getAdapter() != mAutoCompleteAdapter) {
                    lv_searchtext_tips.setAdapter(mAutoCompleteAdapter);
                }
                //更新autoComplete数据
                if (msearchViewListener != null) {
                    msearchViewListener.onRefreshAutoComplete(s + "");
                }
            } else {
                ib_searchtext_delete.setVisibility(GONE);
                lv_searchtext_tips.setVisibility(VISIBLE);
                if (mHintAdapter != null) {
                    lv_searchtext_tips.setAdapter(mHintAdapter);
                }
            }
        }

    }

    /**
     * search view回调方法
     */
    public interface SearchViewListener {

        /**
         * 更新自动补全内容
         *
         * @param text 传入补全后的文本
         */
        void onRefreshAutoComplete(String text);

        /**
         * 开始搜索
         *
         * @param text 传入输入框的文本
         */
        void onSearch(String text);

    }

}

