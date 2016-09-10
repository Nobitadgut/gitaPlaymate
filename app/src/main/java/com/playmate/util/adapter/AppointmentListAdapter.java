package com.playmate.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playmate.R;
import com.playmate.data.AppointmentHolder;
import com.playmate.data.TaskAppointmentBean;
import com.playmate.util.view.CircleImageView;

import java.util.HashMap;
import java.util.List;

public class AppointmentListAdapter extends BaseAdapter {

    private List<TaskAppointmentBean> items;
    private LayoutInflater inflater;
    public boolean[] isSelected;

    public class ViewHolder {
        private CircleImageView iv_userIcon;
        private TextView tv_userName;
        private LinearLayout ll_sexul;
        private TextView tv_age;
        private LinearLayout ll_isPass_passed;
        private LinearLayout ll_isPasss_unpass;
        private TextView tv_price;
        private TextView tv_totalPrice;
        private CheckBox cb_isChoose;
    }

    public AppointmentListAdapter(Context context, List<TaskAppointmentBean> items, boolean[] isSelected) {
        this.items = items;
        this.isSelected = isSelected;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listview_appointment, null);
            holder.iv_userIcon = (CircleImageView) convertView.findViewById(R.id.list_appointment_userIcon);
            holder.tv_userName = (TextView) convertView.findViewById(R.id.list_appointment_tv_userName);
            View view = convertView.findViewById(R.id.listview_appointment_ll_sexul);
            holder.ll_sexul = (LinearLayout) view;
            holder.tv_age = (TextView) view.findViewById(R.id.lab_sexul_age);
            holder.ll_isPass_passed = (LinearLayout) convertView.findViewById(R.id.listview_appointment_ll_isPass_pass);
            holder.ll_isPasss_unpass = (LinearLayout) convertView.findViewById(R.id.listview_appointment_ll_isPass_unPass);
            holder.tv_price = (TextView) convertView.findViewById(R.id.listview_appointment_tv_price);
            holder.tv_totalPrice = (TextView) convertView.findViewById(R.id.listview_appointment_tv_totalPrice);
            holder.cb_isChoose = (CheckBox) convertView.findViewById(R.id.listview_appointment_cb_isChoose);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        Bitmap bm = BitmapFactory.decodeFile(items.get(i).userHeaderUrl);
//        holder.userHeaderIcon.setImageBitmap(bm);
        holder.iv_userIcon.setImageResource(R.drawable.tu3);
        holder.tv_userName.setText(items.get(position).userName);
        if (items.get(position).userGender == 1){
            holder.ll_sexul.setBackgroundResource(R.mipmap.sex1);
        }else {
            holder.ll_sexul.setBackgroundResource(R.mipmap.sex2);
        }
        holder.tv_age.setText(items.get(position).ageStr);
        if (items.get(position).isPass == 1){
            holder.ll_isPass_passed.setVisibility(View.VISIBLE);
            holder.ll_isPasss_unpass.setVisibility(View.GONE);
        }else {
            holder.ll_isPass_passed.setVisibility(View.GONE);
            holder.ll_isPasss_unpass.setVisibility(View.VISIBLE);
        }
        holder.tv_price.setText(items.get(position).singlePriceStr);
        holder.tv_totalPrice.setText(items.get(position).totalPriceStr);
        holder.cb_isChoose.setChecked(isSelected[position]);

        return convertView;
    }
}
