package com.playmate.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playmate.R;
import com.playmate.data.TaskListBean;
import com.playmate.util.view.CircleImageView;

import java.util.List;

public class TaskListViewAdapter extends BaseAdapter {

    private List<TaskListBean> items;
    private LayoutInflater inflater;

    public class ViewHolder{
        CircleImageView userHeaderIcon;
        LinearLayout sexsul;
        TextView age;
        TextView userName;
        TextView taskTitle;
        TextView singlePrice;
        TextView start_end_time;
        TextView address;
        TextView creditScore;
        TextView distance;
    }

    public TaskListViewAdapter(Context context, List<TaskListBean> items) {
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listview_task,null);
            holder.userHeaderIcon = (CircleImageView)convertView.findViewById(R.id.task_list_item_usrHeaderIcon);
            View view = convertView.findViewById(R.id.task_list_item_sexul);
            holder.sexsul = (LinearLayout)view;
            holder.age = (TextView) view.findViewById(R.id.lab_sexul_age);

            holder.userName = (TextView)convertView.findViewById(R.id.task_list_item_userName);

            holder.taskTitle = (TextView)convertView.findViewById(R.id.task_list_item_taskTitle);
            holder.singlePrice = (TextView)convertView.findViewById(R.id.task_list_item_singlePrice);
            holder.start_end_time = (TextView)convertView.findViewById(R.id.task_list_item_start_end_time);
            holder.address = (TextView)convertView.findViewById(R.id.task_list_item_address);

            holder.creditScore = (TextView)convertView.findViewById(R.id.task_list_item_creditScore);
            holder.distance = (TextView)convertView.findViewById(R.id.task_list_item_distance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

//        Bitmap bm = BitmapFactory.decodeFile(items.get(i).userHeaderUrl);
//        holder.userHeaderIcon.setImageBitmap(bm);
        holder.userHeaderIcon.setImageResource(R.drawable.tu3);

        if (items.get(i).user_gender == 1){
            holder.sexsul.setBackgroundResource(R.mipmap.sex1);
        }else {
            holder.sexsul.setBackgroundResource(R.mipmap.sex2);
        }
        holder.age.setText(items.get(i).age);
        holder.userName.setText(items.get(i).user_name);

        holder.taskTitle.setText(items.get(i).task_title);
        holder.singlePrice.setText(items.get(i).event_price);
        holder.start_end_time.setText(items.get(i).event_time);
        holder.address.setText(items.get(i).adress);

        holder.creditScore.setText(items.get(i).credit_Str);
        holder.distance.setText(items.get(i).destince);

        return convertView;
    }

}
