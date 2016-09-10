package com.playmate.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.playmate.R;

public class SignUpPopupWindow extends PopupWindow implements View.OnClickListener {

    //    private LinearLayout dialog;
    private EditText price;
    private TextView timeLen;
    private ImageButton back;
    private Button cancel;
    private Button confirm;

    private View mMenuView;

    public SignUpPopupWindow(Activity context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popupwindow_signup, null);

        price = (EditText) mMenuView.findViewById(R.id.popupwindow_et_price);
        timeLen = (TextView) mMenuView.findViewById(R.id.popupwindow_tv_timeLen);
        back = (ImageButton) mMenuView.findViewById(R.id.popupwindow_ib_back);
        cancel = (Button) mMenuView.findViewById(R.id.popupwindow_button_cancel);
        confirm = (Button) mMenuView.findViewById(R.id.popupwindow_button_confirm);

        back.setOnClickListener(this);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);

        price.setText("90");
        timeLen.setText("2");

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

                int height = mMenuView.findViewById(R.id.popupwindow_ll_dialog).getTop();
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


    private void confirm() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.popupwindow_button_cancel:
                dismiss();
                break;
            case R.id.popupwindow_button_confirm:
                confirm();
                break;
            case R.id.popupwindow_ib_back:
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }
}
