package com.an.base.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p/>拍照辅助类
 * Created by sunshuntao on 2016/5/21.
 */
public class CaptureHelper {

    private final static String TIMESTAMP_FORMAT = "yyyy_MM_dd_HH_mm_ss";//拍照保存为年月日，时分秒的png照片。

    public final static int CAPTURE_PHOTO_REQUEST_CODE = 0x1111;//// Take Picture
    public final static int SELECT_IMAGE_KITKAT = 0x0000;//// // Local Image4.4版本以上。LOLLIPOP21BANBEN
    public final static int IMAGE_REQUEST_CODE = 0x0001;//// // Local Image4.4及4.4版本以下。

    private Activity mActivity;
    /**
     * 存放图片的目录
     */
    private File mPhotoFolder;
    /**
     * 拍照生成的图片文件
     */
    private File mPhotoFile;

    /**
     * @param activity
     * @param photoFolder 存放生成照片的目录，目录不存在时候会自动创建，但不允许为null;
     */
    public CaptureHelper(Activity activity, File photoFolder) {
        this.mActivity = activity;
        this.mPhotoFolder = photoFolder;
    }

    /**
     * 拍照
     */
    public void capture() {
        if (hasCamera()) {
            createPhotoFile();
            if (mPhotoFile == null) {
                Toast.makeText(mActivity, "mPhotofile is null", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri fileUri = Uri.fromFile(mPhotoFile);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            mActivity.startActivityForResult(captureIntent, CAPTURE_PHOTO_REQUEST_CODE);
        } else {
            Toast.makeText(mActivity, "打开错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 创建照片文件
     */
    private void createPhotoFile() {
        if (mPhotoFolder != null) {
            if (!mPhotoFolder.exists()) {//检查保存图片的目录存不存在
                mPhotoFolder.mkdirs();
            }
            String fileName = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
            mPhotoFile = new File(mPhotoFolder, fileName + "png");
            if (mPhotoFile.exists()) {
                mPhotoFile.delete();
            }
            try {
                mPhotoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                mPhotoFile = null;
            }
        } else {
            mPhotoFile = null;
            Toast.makeText(mActivity, "不存在该目录！", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * @return 存在返回true，不存在返回false
     * @ 判断系统中是否存在可以启动的相机应用，私人可以用。
     */
    private boolean hasCamera() {
        PackageManager packageManager = mActivity.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 获取当前拍到的图片文件
     *
     * @return
     */
    public File getPhoto() {
        return mPhotoFile;
    }

    /**
     * 设置照片文件
     *
     * @param photoFile
     */
    public void setPhoto(File photoFile) {
        this.mPhotoFile = photoFile;
    }
}
