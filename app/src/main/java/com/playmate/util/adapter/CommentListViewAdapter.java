package com.playmate.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.playmate.R;
import com.playmate.data.CommentBean;
import com.playmate.util.view.CircleImageView;
import java.util.List;

public class CommentListViewAdapter extends BaseAdapter {

    private List<CommentBean> items;
    private LayoutInflater inflater;

    public class ViewHolder {
        CircleImageView userHeaderIcon;
        TextView userName;
        TextView commentContext;
        TextView sendTime;
    }

    public CommentListViewAdapter(Context context, List<CommentBean> items) {
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listview_comment, null);
            holder.userHeaderIcon = (CircleImageView) convertView.findViewById(R.id.list_comment_userIcon);
            holder.userName = (TextView) convertView.findViewById(R.id.list_comment_userName);
            holder.commentContext = (TextView) convertView.findViewById(R.id.list_comment_commentContext);
            holder.sendTime = (TextView) convertView.findViewById(R.id.list_comment_sendTime);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        Bitmap bm = BitmapFactory.decodeFile(items.get(i).userHeaderUrl);
//        holder.userHeaderIcon.setImageBitmap(bm);
        holder.userHeaderIcon.setImageResource(R.drawable.tu3);
        holder.userName.setText(items.get(position).user_name);
        holder.commentContext.setText(items.get(position).commentContext);
        holder.sendTime.setText(items.get(position).sendTimeString);

        return convertView;
    }
}
