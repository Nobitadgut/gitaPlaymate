package com.playmate.util.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.playmate.activity.BaseWebActivity;

import java.util.ArrayList;

public class MyPagerAdapter extends PagerAdapter {

    private ArrayList<ImageView> imageViewlist;
    private ArrayList<String> urlList;
    private Context context;

    public MyPagerAdapter(final Context context, ArrayList<ImageView> imageViewlist, ArrayList<String> urlList){
        super();
        this.imageViewlist = imageViewlist;
        this.urlList = urlList;
        this.context = context;

        for (int i=0; i<imageViewlist.size(); i++){
            imageViewlist.get(i).setOnClickListener(new myOnclickListener(i));
        }
    }

    private class myOnclickListener implements View.OnClickListener {
        int i;

        public  myOnclickListener(int i){
            super();
            this.i = i;
        }

        @Override
        public void onClick(View view) {
            String url = urlList.get(i);
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            Intent intent = new Intent(context, BaseWebActivity.class);
            intent.putExtras(bundle);

            context.startActivity(intent);
        }
    }

    /**
     * 返回图片总数，Integer.MAX_VALUE的值为 2147483647这个数有21亿，也就是说我们的viewpager
     * 理论上在每次使用应用的时候可以滑动21亿次,当然，实际上是没人要去这么做的，我们这样做是为了实现实现viewpager循环滑动的效果
     * 即当滑动到viewpager的最后一页时，继续滑动就可以回到第一页
     *
     */
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    // 当某一页滑出去的时候，将其销毁
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViewlist.get(position
                % imageViewlist.size()));
    }

    // 向容器中添加图片，由于我们要实现循环滑动的效果，所以要对position取模
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = imageViewlist.get(position % imageViewlist.size());
        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        container.addView(v);

        return imageViewlist.get(position % imageViewlist.size());
    }
}