package com.playmate.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.playmate.R;

public class PickupImagePopupWindow extends PopupWindow implements View.OnClickListener {

    private Button fromPhotos;
    private Button fromCapture;
    private View mMenuView;
    Handler handler;

    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public PickupImagePopupWindow(Activity context, Handler handler) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popupwindow_pickupimage, null);
        fromPhotos = (Button) mMenuView.findViewById(R.id.popupwindow_pickupImage_button_chooseFromPhotos);
        fromCapture = (Button) mMenuView.findViewById(R.id.popupwindow_pickupImage_button_chooseFromCapture);

        this.handler = handler;

        fromCapture.setOnClickListener(this);
        fromPhotos.setOnClickListener(this);

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.popupwindow_complete_ll_dialog).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        Message message = Message.obtain();
        switch (view.getId()) {
            case R.id.popupwindow_pickupImage_button_chooseFromPhotos:
                message.what = REQUEST_IMAGE_GET;
                handler.sendMessage(message);
                dismiss();
                break;
            case R.id.popupwindow_pickupImage_button_chooseFromCapture:
                message.what = REQUEST_IMAGE_CAPTURE;
                handler.sendMessage(message);
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }
}
