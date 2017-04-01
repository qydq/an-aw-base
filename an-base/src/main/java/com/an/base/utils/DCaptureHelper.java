package com.an.base.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p/>拍照辅助类
 * Created by sunshuntao on 2016/5/21.
 */
public class DCaptureHelper {

    private final static String TIMESTAMP_FORMAT = "yyyy_MM_dd_HH_mm_ss";//拍照保存为年月日，时分秒的jpg照片。
    public final static int RESULT_CAPTURE_CODE = 0x0011;// 拍照后返回结果码
    public final static int RESULT_CAPTURE_CROP_CODE = 0x0001;// 拍照后裁剪返回结果码
    public final static int RESULT_PHOTO_CROP_CODE = 0x0000;// 选择相册后裁剪返回结果码
    public final static int RESULT_PHOTO_CODE = 0x0101;//选择相册后返回结果码

    public static final int CAMERA_REQUEST_CODE = 1;//相机请求权限

    private Activity mActivity;
    /**
     * 存放图片的目录
     */
    private File skRoot;
    /**
     * 拍照生成的图片文件
     */
    private File mPhotoFile;
    private Uri mFileUri;
    private Uri imageUri;
    private Uri imageCropUri;

    /**
     * @param activity
     * @param skRoot   存放生成照片的目录，目录不存在时候会自动创建，但不允许为null;
     */
    public DCaptureHelper(Activity activity, File skRoot) {
        this.mActivity = activity;
        this.skRoot = skRoot;
    }

    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "face.jpg";
    private static final String IMAGE_FILE_MAX_NAME = "face_max.jpg";

    /**
     * 拍照，以时间命名，可以调用getPhoto得到时间命名的file照片。
     */
    public void capture() {
        if (hasCamera()) {
            //android6.0以上实现StrictMode API政策禁
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d("DCaptureHelper", "--yy@@--方法名--androidM--");
                final String permission = Manifest.permission.CAMERA;  //相机权限
                final String permission1 = Manifest.permission.WRITE_EXTERNAL_STORAGE; //写入数据权限
                if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mActivity, permission1) != PackageManager.PERMISSION_GRANTED) {  //先判断是否被赋予权限，没有则申请权限
                    if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {  //给出权限申请说明
                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                    } else { //直接申请权限
                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE); //申请权限，可同时申请多个权限，并根据用户是否赋予权限进行判断
                    }
                } else {  //赋予过权限，则直接调用相机拍照
                    startCamera();
                }
            } else {
                Log.d("DCaptureHelper", "--yy@@--方法名--androidO--");
                startCamera();
            }

        } else {
            Toast.makeText(mActivity, "没有检测到相机", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开相机获取图片
     */
    private void startCamera() {
        createPhotoFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("DCaptureHelper", "--yy@@--方法名--androidM1--");
            //第二参数是在manifest.xml定义 provider的authorities属性
            mFileUri = FileProvider.getUriForFile(mActivity, "com.an.base.takephoto.fileprovider", mPhotoFile);
        } else {
            Log.d("DCaptureHelper", "--yy@@--方法名--androidO1--");
            mFileUri = Uri.fromFile(mPhotoFile);
        }

        //兼容版本处理，因为 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) 只在5.0以上的版本有效

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip = ClipData.newUri(mActivity.getContentResolver(), "A photo", mFileUri);
            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList = mActivity.getPackageManager()
                    .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                mActivity.grantUriPermission(packageName, mFileUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
        mActivity.startActivityForResult(intent, RESULT_CAPTURE_CODE);
    }

    /**
     * @param saveName          拍照后保存的名字
     * @param isNoFaceDetection 不开启人脸识别拍照
     */
    public void capture(String saveName, boolean isNoFaceDetection) {
        if (hasCamera()) {
            Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentFromCapture.putExtra("return-data", false);
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(skRoot, saveName)));
            intentFromCapture.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intentFromCapture.putExtra("noFaceDetection", isNoFaceDetection);
            mActivity.startActivityForResult(intentFromCapture, RESULT_CAPTURE_CODE);
        } else {
            Toast.makeText(mActivity, "没有检测到相机", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 调用系统相册，并返回RESULT_PHOTO_CODE
     */
    public void photo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        mActivity.startActivityForResult(intent, RESULT_PHOTO_CODE);//4.4版本以上。
    }

    /**
     * 创建照片文件
     */
    private void createPhotoFile() {
        if (skRoot != null) {
//            拍照保存为年月日，时分秒的jpg照片。存在mPhotoFile目录下面
            String fileName = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
            mPhotoFile = new File(skRoot, fileName + ".jpg");
        } else {
            Toast.makeText(mActivity, "不存在该目录！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param uri            原始uri。一般未data.getData()  裁剪图片方法实现,系统会自动根据android版本调用合适的方法。
     * @param cropWidthSize  裁剪的宽度
     * @param cropHeightSize 裁剪的高度
     **/
    public void startPhotoZoom(Uri uri, int cropWidthSize, int cropHeightSize) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
            return;
        }
        //默认
        if (cropHeightSize < 150) {
            cropWidthSize = 150;
            cropHeightSize = 150;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String url = getPath(mActivity, uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String url = getPath(mActivity, uri);
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
        intent.putExtra("outputX", cropWidthSize);
        intent.putExtra("outputY", cropHeightSize);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        mActivity.startActivityForResult(intent, RESULT_PHOTO_CROP_CODE);
    }

    /**
     * @param uri            原始uri。一般未data.getData()  裁剪图片方法实现,系统会自动根据android版本调用合适的方法。
     * @param cropName       裁剪并保存名为cropName的jpg照片
     * @param cropWidthSize  裁剪的宽度
     * @param cropHeightSize 裁剪的高度
     */
    public void startPhotoZoom(Uri uri, String cropName, int cropWidthSize, int cropHeightSize) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
            return;
        }
        String cropPath = skRoot + "/" + cropName;
        File cropFile = new File(cropPath);
        Uri imageCropUri = Uri.fromFile(cropFile);//裁剪后图片的Uri
        //默认
        if (cropHeightSize < 150) {
            cropWidthSize = 150;
            cropHeightSize = 150;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Uri outputUri = FileProvider.getUriForFile(mActivity, "com.an.base.takephoto.fileprovider", cropFile);
            Uri imageUri = FileProvider.getUriForFile(mActivity, "com.an.base.takephoto.fileprovider", cropFile);//通过FileProvider创建一个content类型的Uri
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String url = getPath(mActivity, uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }

        String url = getPath(mActivity, uri);
        intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", cropWidthSize);
        intent.putExtra("outputY", cropHeightSize);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);//输出裁剪后的名字为face.jpg
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        mActivity.startActivityForResult(intent, RESULT_PHOTO_CROP_CODE);
    }


    /**
     * @param originName
     * @param cropName
     */
    public void startCaptureZoom(String originName, String cropName) {
        //拍照的原始未裁剪照片face_max.jpg
        String imagePath = skRoot + File.separator + originName;
        File imageFile = new File(imagePath);
        Uri imageUri = Uri.fromFile(imageFile);//原始图片的Uri

        String cropPath = skRoot + File.separator + cropName;
        File cropFile = new File(cropPath);
        Uri imageCropUri = Uri.fromFile(cropFile);//裁剪后图片的Uri

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 700);
        intent.putExtra("outputY", 700);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);//输出裁剪后的名字为face.jpg
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        mActivity.startActivityForResult(intent, RESULT_CAPTURE_CROP_CODE);
    }

    /**
     * @param cropName
     */
    public void startCaptureZoom(String cropName) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        String cropPath = skRoot + File.separator + cropName;
        File cropFile = new File(cropPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(mActivity, "com.an.base.takephoto.fileprovider", mPhotoFile);
            imageCropUri = FileProvider.getUriForFile(mActivity, "com.an.base.takephoto.fileprovider", cropFile);
        } else {
            //原始图片的Uri
            imageUri = Uri.fromFile(mPhotoFile);
            //裁剪后图片的Uri
            imageCropUri = Uri.fromFile(cropFile);
        }
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 700);
        intent.putExtra("outputY", 700);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);//输出裁剪后的名字为face.jpg
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        mActivity.startActivityForResult(intent, RESULT_CAPTURE_CROP_CODE);
    }

    /**
     * @param mBitmap  bitmap
     * @param saveName 保存的名字
     * @ 该方法单独使用，保存名为saveName的照片到内存卡
     */
    public void saveBitmap(Bitmap mBitmap, String saveName) {
        File f = new File(skRoot, saveName);
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
}
