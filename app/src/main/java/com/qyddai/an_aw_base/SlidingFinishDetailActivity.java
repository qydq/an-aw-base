package com.qyddai.an_aw_base;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.an.base.model.utils.XCallBack;
import com.an.base.model.utils.XHttps;
import com.an.base.utils.DUtilsBitmap;
import com.an.base.view.activity.SwipeFinishActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

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
    private Uri imageUri;
    private Uri imageCropUri;

    private String[] items = new String[]{"图库", "拍照"};
    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "face.jpg";
    private static final String IMAGE_FILE_MAX_NAME = "face_max.jpg";
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int SELECT_PIC_KITKAT = 3;
    private static final int RESULT_CAMERA_CROP_PATH_RESULT = 4;
    private Bitmap bitmap;
    private String path;
    private File f;

    @Override
    public void initView() {
        anTvTitle = (TextView) findViewById(R.id.anTvTitle);
        anTvTitle.setText(R.string.SlidingFinishDetailActivity);
        path = Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME;
        f = new File(path);
        imageCropUri = Uri.fromFile(f);
        if (f.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            Bitmap roundBitmap = DUtilsBitmap.INSTANCE.zoomBitmap(bitmap, 120, 120);
            iv.setImageBitmap(roundBitmap);
        }
        String imagePath = Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_MAX_NAME;
        File imageFile = new File(imagePath);
        imageUri = Uri.fromFile(imageFile);
        anTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 判断SD卡是否存在。返回true代表存在，false代表不存在；
                // 特别说明：针对不同的Android手机有的厂商没有为手机配置SD卡，像三星有几款手机不具有拓展内存的。
                // 废话再多一点补充：
                // 这里要区别一下SD卡，外部存储卡，内部存储卡，运行内存这些都是不同的概念。不是特别理解的同学请查一下google或者关注我的博客里面有一篇文章是介绍这些概念的。
                // 比如说我要写一个相册的程序，图片肯定是存在外部的存储卡中，而如果我需要的是存储一些配置信息则是放在内部存储卡中。
                //操作一个文件（读写，创建文件或者目录）是通过File类来完成的，这个操作和java中完全一致。
//        外部存储external storage和内部存储internal storage
                if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                    showSettingFaceDialog();
//                    File skRoot = Environment.getExternalStorageDirectory();
//                    CaptureHelper captureHelper = new CaptureHelper(SlidingFinishDetailActivity.this, skRoot);
//                    captureHelper.capture();
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
                            case 0:// Local Image
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("image/*");
//                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                    startActivityForResult(intent, SELECT_PIC_KITKAT);//4.4版本以上。
                                } else {
                                    startActivityForResult(intent, IMAGE_REQUEST_CODE);//4.4及4.4版本以下。
                                }
                                break;
                            case 1:// Take Picture
                                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                intentFromCapture.putExtra("return-data", false);
                                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                                                IMAGE_FILE_MAX_NAME)));
                                intentFromCapture.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                                intentFromCapture.putExtra("noFaceDetection", true);
                                startActivityForResult(intentFromCapture,
                                        CAMERA_REQUEST_CODE);
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

    public void cropImg(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 700);
        intent.putExtra("outputY", 700);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, RESULT_CAMERA_CROP_PATH_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                //Local Image一下俩。
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case SELECT_PIC_KITKAT:
                    startPhotoZoom(data.getData());
                    break;
                //照相机Take Picture
                case CAMERA_REQUEST_CODE:
                    cropImg(imageUri);
                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        setImageToView(data, iv);
                    }
                    break;
                case RESULT_CAMERA_CROP_PATH_RESULT:
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

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url = getPath(mContext, uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void setImageToView(Intent data, ImageView imageView) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Bitmap roundBitmap = DUtilsBitmap.INSTANCE.createReflectionImageWithOrigin(photo);
            imageView.setImageBitmap(roundBitmap);
            saveBitmap(photo);
        }
    }

    public void saveBitmap(Bitmap mBitmap) {
        File f = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
        try {
            f.createNewFile();
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @ Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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
