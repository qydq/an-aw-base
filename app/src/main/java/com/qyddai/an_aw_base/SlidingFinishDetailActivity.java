package com.qyddai.an_aw_base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.an.base.model.utils.XCallBack;
import com.an.base.model.utils.XHttps;
import com.an.base.utils.CaptureHelper;
import com.an.base.utils.DUtilsBitmap;
import com.an.base.view.activity.SwipeFinishActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.Map;

import static com.an.base.utils.CaptureHelper.RESULT_CAPTURE_CODE;
import static com.an.base.utils.CaptureHelper.RESULT_CAPTURE_CROP_CODE;
import static com.an.base.utils.CaptureHelper.RESULT_PHOTO_CODE;
import static com.an.base.utils.CaptureHelper.RESULT_PHOTO_CROP_CODE;

/**
 * Created by qydda on 2016/12/6.
 */

@ContentView(R.layout.sst_activity_finishdetail)
public class SlidingFinishDetailActivity extends SwipeFinishActivity {
    //以下为headview_standard.xml
    @ViewInject(R.id.anLlBack)
    private LinearLayout anLlBack;
    @ViewInject(R.id.anTvBack)
    private TextView anTvBack;
    @ViewInject(R.id.anPb)
    private ProgressBar anPb;
    @ViewInject(R.id.anTvTitle)
    private TextView anTvTitle;

    @ViewInject(R.id.anLlRight)
    private LinearLayout anLlRight;
    @ViewInject(R.id.anTvRight)
    private TextView anTvRight;
    @ViewInject(R.id.anIvRight)
    private ImageView anIvRight;

    //分割线0000---complex
    @ViewInject(R.id.anLlRRight)
    private LinearLayout anLlRRight;
    @ViewInject(R.id.anTvRRight)
    private TextView anTvRRight;
    @ViewInject(R.id.anIvRRight)
    private ImageView anIvRRight;
    @ViewInject(R.id.iv)
    private ImageView iv;

    private String[] items = new String[]{"图库", "拍照"};
    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "face.jpg";
    private static final String IMAGE_FILE_MAX_NAME = "face_max.jpg";
    /* 请求码 */
    private static final int CAMERA_REQUEST_CODE = 1;

    private CaptureHelper captureHelper;//拍照辅助类
    private File skRoot;//根路径
    private Uri imageUri;//原始Uri
    private Uri imageCropUri;//裁剪后Uri

    @Override
    public void initView() {
        anTvTitle = (TextView) findViewById(R.id.anTvTitle);
        anTvTitle.setText(R.string.SlidingFinishDetailActivity);
        //加载本地裁剪后SD卡的图片。face.jpg
        skRoot = Environment.getExternalStorageDirectory();
        String path = skRoot + "/" + IMAGE_FILE_NAME;
        File file = new File(path);
        imageCropUri = Uri.fromFile(file);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            iv.setImageBitmap(bitmap);
        }
        //拍照的原始未裁剪照片face_max.jpg
        String imagePath = skRoot + "/" + IMAGE_FILE_MAX_NAME;
        File imageFile = new File(imagePath);
        imageUri = Uri.fromFile(imageFile);
        //初始化CaptureHelper
        captureHelper = new CaptureHelper(SlidingFinishDetailActivity.this, skRoot);
        anTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (skRoot.equals(android.os.Environment.MEDIA_MOUNTED)) {
                    showSettingFaceDialog();
                } else {
                    showToast("没有sd卡");
                }
            }
        });
    }


    private void showSettingFaceDialog() {

        new AlertDialog.Builder(this)
                .setTitle("图片来源")
                .setCancelable(true)
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:// Local Image(调用相册会返回PHOTO_REQUEST_LOLLIPOP_CODE，PHOTO_REQUEST_KITKAT_CODE)
                                captureHelper.photo();
                                break;
                            case 1:// Take Picture
                                captureHelper.capture(IMAGE_FILE_MAX_NAME, true);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                //选取照片后返回。
                case RESULT_PHOTO_CODE:
                    //需要裁剪则调用该方法。
                    captureHelper.startPhotoZoom(data.getData(), 200, 200);
                    break;
                //选取照片裁剪后返回。可以在这里设置图片显示到控件中。
                case RESULT_PHOTO_CROP_CODE:
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            Bitmap bitmap = extras.getParcelable("data");
                            Bitmap roundBitmap = DUtilsBitmap.INSTANCE.createReflectionImageWithOrigin(bitmap);
                            iv.setImageBitmap(roundBitmap);
                            //保存
                            captureHelper.saveBitmap(bitmap, IMAGE_FILE_NAME);
                        }
                    }
                    break;
                //照相机照相以后返回。
                case RESULT_CAPTURE_CODE:
                    //需要裁剪照相后的图片调用该方法。（会保存名为imageCropUri的值）
                    captureHelper.cropImg(imageUri, imageCropUri);
                    break;
                //照相机拍照后裁剪图片的显示
                case RESULT_CAPTURE_CROP_CODE:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageCropUri));
                            iv.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadFace() {
        File file = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
        String filePath = file.getAbsolutePath();

        Log.i("tag", "filePath=" + filePath);
        Map<String, Object> map = new ArrayMap<>();
        map.put("file", file);
        map.put("xutils3", "sunshuntao");
        XHttps.upLoadFile("https://www.bgwan.image/uploaddata", map, new XCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                //可以根据公司的需求进行统一的请求成功的逻辑处理
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //可以根据公司的需求进行统一的请求网络失败的逻辑处理
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
