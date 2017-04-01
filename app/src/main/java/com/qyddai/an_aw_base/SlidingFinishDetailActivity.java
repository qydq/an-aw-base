package com.qyddai.an_aw_base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.an.base.model.ytips.XCallBack;
import com.an.base.model.ytips.XHttps;
import com.an.base.utils.CaptureHelper;
import com.an.base.utils.DUtilsBitmap;
import com.an.base.utils.DUtilsStorage;
import com.an.base.view.activity.SwipeFinishActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import static com.an.base.utils.CaptureHelper.CAMERA_REQUEST_PERMISSION_CODE;
import static com.an.base.utils.CaptureHelper.PHOTO_REQUEST_PERMISSION_CODE;
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
    private static final String RESULT_IMAGE_NAME = "shiluohua";//最终的图片名
    private static final String IMAGE_CROP_NAME = "shiluohua_crop";//裁剪后保存的图片名字
    private static final String CAMERA_IMAGE_NAME = "shiluohua_camera";//照相拍照后保存的名字
    /* 请求码 */

    private CaptureHelper captureHelper;//拍照辅助类
    private File skRoot;//根路径(可以调用captureHelper.getSkRoot())

    private static Uri resultUri;//保存后。原最终的图片Uri
    private static File resultFile;//保存后。原最终的图片

    public static String AnTAG = "an_ytips";//an框架系列的资源文件都保存在an_ytips目录下面
    public static String AnPictureTAG = "picture";

    public boolean isCrop = false;

    @Override
    public void initView() {
        anTvTitle = (TextView) findViewById(R.id.anTvTitle);
        anTvRight = (TextView) findViewById(R.id.anTvRight);

        //加载本地裁剪后SD卡的图片。shiluohua.jpg
        skRoot = new File(DUtilsStorage.INSTANCE.getskRootFile(), AnTAG + File.separator + AnPictureTAG);

        //初始化CaptureHelper
        captureHelper = new CaptureHelper(SlidingFinishDetailActivity.this, skRoot);

/*        captureHelper = new DCaptureHelper(SlidingFinishDetailActivity.this);
        skRoot = captureHelper.getSkRoot();*/

        //只有保存后才可以去得到图片。
        String path = skRoot + File.separator + RESULT_IMAGE_NAME + ".jpg";

        resultFile = new File(path);

        if (resultFile.exists()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //通过FileProvider创建一个content类型的Uri
                resultUri = FileProvider.getUriForFile(this, "com.an.base.fileprovider.takephoto", resultFile);
            } else {
                //原始图片的Uri
                resultUri = Uri.fromFile(resultFile);
                //低版本可以用该方法加载内存中的一张图片。
//                Bitmap bitmap = BitmapFactory.decodeFile(path);
//                iv.setImageBitmap(bitmap);
            }
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(resultUri));
                iv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        anTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettingFaceDialog();
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
                            case 0:
                                // Local Image调用相册会返回
                                // RESULT_PHOTO_CODE
                                captureHelper.photo();
                                break;
                            case 1:
                                // 拍照，保存名为CAMERA_IMAGE_NAME的jpg
                                // 返回未RESULT_CAPTURE_CODE
//                                captureHelper.capture();
                                captureHelper.capture(CAMERA_IMAGE_NAME, true);
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
                    if (data != null) {

                        //是否需要裁剪，如果需要则裁剪，不需要则显示出图片
                        if (isCrop) {
                            //选取照片需要裁剪则调用该方法。
//                        captureHelper.startPhotoZoom(data, 200, 200);//默认裁剪
                            captureHelper.startPhotoZoom(data, IMAGE_CROP_NAME, 200, 200);//保存文件名为IMAGE_CROP_NAME的裁剪图片
                        } else {
                            Bitmap bitmap = captureHelper.getCompressBitmap(getApplication(), data);
//                            int degree = PhotoRotateUtil.getBitmapDegree(data.getData().getPath());
//                            bitmap = rotateBitmapByDegree(bitmap, degree);
                            if (bitmap != null)
                                iv.setImageBitmap(bitmap);
                        }
//                            captureHelper.saveBitmap(bitmap, RESULT_IMAGE_NAME);
                    }

                    break;
                //选取照片裁剪后返回。可以在这里设置图片显示到控件中。
                case RESULT_PHOTO_CROP_CODE:
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            Bitmap bitmap = extras.getParcelable("data");
                            Bitmap roundBitmap = DUtilsBitmap.INSTANCE.createReflectionImageWithOrigin(bitmap);
                            iv.setImageBitmap(roundBitmap);
                            //保存(有深意)
                            captureHelper.saveBitmap(bitmap, RESULT_IMAGE_NAME);
                        }
                    }
                    break;
                //照相机照相以后返回。
                case RESULT_CAPTURE_CODE:
                    //需要裁剪照相后的图片调用该方法。（会保存名为IMAGE_FILE_NAME的值）
//                    captureHelper.startCaptureZoom(IMAGE_CROP_NAME);
                    captureHelper.startCaptureZoom();
                    break;
                //照相机拍照后裁剪图片的返回
                case RESULT_CAPTURE_CROP_CODE:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        try {
//                            //imageCropUri
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(captureHelper.getImageCropUri()));
                            iv.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }

        super.

                onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_PERMISSION_CODE:
                int lengthCamera = grantResults.length;
                final boolean isGranted = lengthCamera >= 1 && PackageManager.PERMISSION_GRANTED == grantResults[lengthCamera - 1];
                if (isGranted) {  //如果用户赋予权限，则调用相机
                    captureHelper.capture();//给了权限后去capture
                } else {
                    showToast("不给拍照权限你怪我了。");
                }
                break;
            case PHOTO_REQUEST_PERMISSION_CODE:
                int lengthPhoto = grantResults.length;
                if (lengthPhoto > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    captureHelper.photo();//给了权限后去photo
                } else {
                    showToast("不给读照片权限你怪我了。");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void uploadFace() {
        File file = new File(Environment.getExternalStorageDirectory(), RESULT_IMAGE_NAME);
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
