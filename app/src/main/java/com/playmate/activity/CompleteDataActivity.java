package com.playmate.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.playmate.R;
import com.playmate.util.CustomProgress;
import com.playmate.util.HttpUtil;
import com.playmate.util.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CompleteDataActivity extends Activity implements View.OnTouchListener{

    private  Activity thisActivity;
    private ImageButton back;
    private CircleImageView userIcon;
    private EditText userName;
    private EditText birth;
    private Button register;
    private RadioGroup genderGroup;
    private RadioButton male;
    private RadioButton female;

    private int sexul;
    private long userId;
    private String userLoginPhone;
    private String userNameStr;
    private String birthStr;
    private String registerMessage;

    private String mCurrentPhotoPath;
    private CustomProgress progressDialog;
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int COMPLETE_DATA_SUCCEED = 2;
    private static final int COMPLETE_DATA_FAILD = 3;

    private Handler handler = new Handler() {

        // 处理子线程给我们发送的消息。
        @Override
        public void handleMessage(android.os.Message msg) {

            switch (msg.what){
                case REQUEST_IMAGE_GET:
                    selectImage();
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    dispatchTakePictureIntent();
                    break;
                case COMPLETE_DATA_SUCCEED:
                    onComeleteSucceed();
                    break;
                case COMPLETE_DATA_FAILD:
                    onCompleteFalid();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completedata);
        thisActivity = this;

        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        if (bundle == null) {
            Toast.makeText(this, "网络连接失败，请检测网络！", Toast.LENGTH_LONG).show();
            return;
        }

        userId = bundle.getLong("userId", 0);
        userLoginPhone = bundle.getString("userLoginPhone", "");

        initView();
        setListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 回调成功
        if (resultCode == RESULT_OK) {
            String filePath = null;
            //判断是哪一个的回调
            if (requestCode == REQUEST_IMAGE_GET) {
                //返回的是content://的样式
                filePath = getFilePathFromContentUri(data.getData(), this);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (mCurrentPhotoPath != null) {
                    filePath = mCurrentPhotoPath;
                }
            }
            if (!TextUtils.isEmpty(filePath)) {
                // 自定义大小，防止OOM
                Bitmap bitmap = getSmallBitmap(filePath, 200, 200);
                userIcon.setImageBitmap(bitmap);
            }
        }
    }

    private void initView(){
        back = (ImageButton)findViewById(R.id.activity_completeData_ib_back);
        userIcon = (CircleImageView)findViewById(R.id.activity_completeData_iv_userIcon);
        userName = (EditText)findViewById(R.id.activity_completeData_et_usrName);
        birth = (EditText)findViewById(R.id.activity_completeData_et_birth);
        register = (Button)findViewById(R.id.activity_completeData_button_register);
        genderGroup = (RadioGroup)findViewById(R.id.activity_completeData_rg_genderGroup);
        male = (RadioButton)findViewById(R.id.activity_completeData_rb_male);
        female = (RadioButton)findViewById(R.id.activity_completeData_rb_female);
    }

    private void setListener(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == male.getId()){
                    sexul = 0;
                }else if(checkedId == female.getId()){
                    sexul = 1;
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeData();
            }
        });

        birth.setOnTouchListener(this);

        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickupImagePopupWindow menuWindow = new PickupImagePopupWindow(thisActivity, handler);
                menuWindow.showAtLocation(thisActivity.findViewById(R.id.activity_completeData_ll_parent), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    private void onComeleteSucceed(){
        thisActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Intent intent = new Intent(thisActivity, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void onCompleteFalid(){
        thisActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (registerMessage.equals("")) registerMessage = "网络错误";
                Toast.makeText(thisActivity, registerMessage, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void completeData(){
        birthStr = birth.getText().toString();
        userNameStr = userName.getText().toString();
        String jstr;
        JSONObject data = new JSONObject();
        try {
            data.put("userId", userId);
            data.put("userLoginPhone", userLoginPhone);
            data.put("userName", userNameStr);
            data.put("userGender", sexul);
            data.put("userBirthday", birthStr);
            jstr = data.toString();
        } catch (JSONException e) {
            jstr = "";
        }

        CompleteDateThread completeDateThread = new CompleteDateThread(jstr);
        completeDateThread.start();

        progressDialog = CustomProgress.show(this, "注册中...", true, null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            final StringBuffer time = new StringBuffer();
            DatePickerDialog datePickerDialog = new DatePickerDialog(thisActivity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT ,new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    time.append(year + "-" + (monthOfYear) + "-" + dayOfMonth);
                    birth.setText(time.toString());
                }
            }, 1990, 1, 1);
            datePickerDialog.getDatePicker().setCalendarViewShown(true);
            datePickerDialog.getDatePicker().setSpinnersShown(false);
            datePickerDialog.show();
        }
        return true;
    }

    class CompleteDateThread extends Thread{
        private String jstr;
        private String url;
        private String otherPram;

        public CompleteDateThread(String jstr){
            this.jstr = jstr;
            this.url = "http://120.25.56.82:9100//user/crud";
            this.otherPram = "&opera=update";
        }

        @Override
        public void run() {
            String resultDate = HttpUtil.getHttpData(url, jstr, otherPram);
            try {
                //TODO create login bean
                JSONObject resultJo = new JSONObject(resultDate);
                String code = resultJo.getString("code");
                registerMessage = resultJo.getString("message");
                if (code.equals("1")){
                    System.out.println(code + ":" + registerMessage);
                }else {
                    System.out.println(code + ":" + registerMessage);
                }
            } catch (JSONException e) {
//                Toast.makeText(thisActivity, "数据错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 从相册中获取
     */
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        //判断系统中是否有处理该Intent的Activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        } else {
            Toast.makeText(thisActivity, "未找到图片查看器", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 创建新文件
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* 文件名 */
                ".jpg",         /* 后缀 */
                storageDir      /* 路径 */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断系统中是否有处理该Intent的Activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            // 创建文件来保存拍的照片
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // 异常处理
            }
            if (photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            Toast.makeText(thisActivity, "无法启动相机", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param uri     content:// 样式
     * @param context
     * @return real file path
     */
    public static String getFilePathFromContentUri(Uri uri, Context context) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor == null) return null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    /**
     * 获取小图片，防止OOM
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeFile(filePath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片缩放比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
