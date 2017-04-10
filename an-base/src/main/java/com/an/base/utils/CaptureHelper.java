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
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.an.base.utils.ytips.PermissionUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p/>拍照辅助类CaptureHelper
 * Created by sunshuntao 莳萝花on 2016/5/21.
 * <p>不适用缓存，以后个人开发杜绝采用缓存。
 * 修改日期：2017年4月1日13:23:40（愚人节）
 */
public class CaptureHelper {
    private final static String TAG = "CaptureHelper";
    private final static String TIMESTAMP_FORMAT = "yyyy_MM_dd_HH_mm_ss";//拍照保存为年月日，时分秒的jpg照片。
    public final static int RESULT_CAPTURE_CODE = 0x0001;// 拍照后返回结果码
    public final static int RESULT_CAPTURE_CROP_CODE = 0x0002;// 拍照后裁剪返回结果码
    public final static int RESULT_PHOTO_CROP_CODE = 0x0003;// 选择相册后裁剪返回结果码
    public final static int RESULT_PHOTO_CODE = 0x0004;//选择相册后返回结果码


    public static final int CAMERA_REQUEST_PERMISSION_CODE = 1;//相机请求权限
    public static final int PHOTO_REQUEST_PERMISSION_CODE = 2;//相册请求权限

    public static final int SELF_AN = 1;//采用an框架来存储照片an_ytips/picture
    public static final int SELF_CONTEXT = 0;//采用/data/data/包名/cache/picture

    //读写，照相权限。
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};//PERMISSIONS


    private Activity mActivity;//上下文窗口。
    private Context mContext;//上下文
    private int mFileMode;

    private PermissionUtils permissionUtils;
    /**
     * 存放图片的目录
     */
    private static File skRoot;//创建存放图片的目录文件
    /**
     * 拍照生成的图片文件
     */
    private static File mPhotoFile;//原始图片全路径地址
    private static File cropFile;//裁剪的文件的File

    private static Uri mPhotoUri;//原始图片全Uri途径
    private static Uri imageCropUri;//裁剪文件的Uri


    public static String AnTAG = "an_ytips";//an框架系列的资源文件都保存在an_ytips目录下面
    public static String AnPictureTAG = "picture";
    public static String AnDefaultPicName = "default";

    private static File resultFile;//保存后。原最终的图片
    private static Uri resultUri;//保存后。原最终的图片Uri


    public File getResultFile() {
        return resultFile;
    }

    public void setResultFile(File resultFile) {
        this.resultFile = resultFile;
    }

    public File getSkRoot() {
        return skRoot;
    }

    public Uri getImageCropUri() {
        return imageCropUri;
    }

    public File getmPhotoFile() {
        return mPhotoFile;
    }

    public File getCropFile() {
        return cropFile;
    }

    public Uri getmPhotoUri() {
        return mPhotoUri;
    }

    /**
     * @param activity
     */
    public CaptureHelper(@NonNull Activity activity, int fileMode) {
        this.mActivity = activity;
        mContext = activity.getApplicationContext();
        this.mFileMode = fileMode;
        permissionUtils = new PermissionUtils(activity, mContext);
    }

    /*
    * 初始化,如果是SELE_context模式则不需要6.0或者4.4不需要权限。
    * */
    private void createRootFile() {
        if (mFileMode == SELF_CONTEXT) {
            skRoot = new File(mContext.getCacheDir(), AnPictureTAG);//data/data/com.package/cache/picture
            if (!skRoot.exists())
                skRoot.mkdirs();
        } else {
            skRoot = new File(YstorageUtils.INSTANCE.getskRootFile(), AnTAG + File.separator + AnPictureTAG);//root/an_ytips/picture
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!skRoot.exists())
                    skRoot.mkdirs();
            } else {
                //android6.0+以上系统，需要动态申请权限创建更目录下文件
                if (permissionUtils.lacksPermissions(PERMISSIONS)) {
                    final String permission = Manifest.permission.CAMERA;  //相机权限
//                    permissionUtils.requestMultiPermissions(mPermissionGrant);
//                    permissionUtils.requestPermission(PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
//                    permissionUtils.requestPermission(PermissionUtils.CODE_CAMERA, mPermissionGrant);
                    permissionUtils.requestPermissions(PERMISSIONS, CAMERA_REQUEST_PERMISSION_CODE, permission);

                } else {
                    if (!skRoot.exists())
                        skRoot.mkdirs();
                }
            }
        }
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    if (!skRoot.exists())
                        skRoot.mkdirs();
                    break;
                case PermissionUtils.CODE_CAMERA:

                    break;
                default:
                    break;
            }
        }
    };
    /*
    * -----------------------Fengexian------------------
    *
    * */

    /**
     * 拍照，以时间命名，可以调用getPhoto得到时间命名的file照片。
     * 默认会在系统保存时间命名的图片。。。
     */
    public void capture() {
        if (hasCamera()) {
            //android6.0以上实现StrictMode API政策禁
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionUtils.lacksPermissions(PERMISSIONS)) {
//                    permissionUtils.requestPermission(PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
//                    permissionUtils.requestPermission(PermissionUtils.CODE_CAMERA, mPermissionGrant);
                    final String permission = Manifest.permission.CAMERA;  //相机权限
                    permissionUtils.requestPermissions(PERMISSIONS, CAMERA_REQUEST_PERMISSION_CODE, permission);
                } else {
                    startCamera();
                }
            } else {
                startCamera();//低版本则不用考虑那么多
            }
        } else {
            Toast.makeText(mActivity, "没有检测到相机", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开相机获取图片
     */
    private void startCamera() {
        createRootFile();
        createPhotoFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //第二参数是在manifest.xml定义 provider的authorities属性
            mPhotoUri = FileProvider.getUriForFile(mActivity, "com.an.base.fileprovider.tackphoto", mPhotoFile);
        } else {
            mPhotoUri = Uri.fromFile(mPhotoFile);
        }
        //兼容版本处理，因为 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) 只在5.0以上的版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip = ClipData.newUri(mActivity.getContentResolver(), "A photo", mPhotoUri);
            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList = mActivity.getPackageManager()
                    .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                mActivity.grantUriPermission(packageName, mPhotoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        mActivity.startActivityForResult(intent, RESULT_CAPTURE_CODE);
    }
 /*
    *
    * 私有方法*/

    /**
     * 创建照片文件-time类型
     */
    private void createPhotoFile() {
        if (skRoot != null) {
            String dataFormat = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
            mPhotoFile = new File(skRoot, dataFormat + ".jpg");
        } else {
            Toast.makeText(mActivity, "不存在skRoot该目录！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 裁剪照片-time类型（裁剪后图片保存为时间类型的图片）
     */
    public void startCaptureZoom() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        //获取当前时间
        String dataFormat = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
        String cropPath = skRoot + File.separator + dataFormat + ".jpg";
        cropFile = new File(cropPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//零时权限
            //通过FileProvider创建一个content类型的Uri
            mPhotoUri = FileProvider.getUriForFile(mActivity, "com.an.base.fileprovider.tackphoto", mPhotoFile);
            imageCropUri = FileProvider.getUriForFile(mActivity, "com.an.base.fileprovider.tackphoto", cropFile);
        } else {
            //原始图片的Uri
            mPhotoUri = Uri.fromFile(mPhotoFile);
            //裁剪后图片的Uri
            imageCropUri = Uri.fromFile(cropFile);
        }
        intent.setDataAndType(mPhotoUri, "image/*");
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

//    -----------------------分割线----------2017年4月1日 10:12:29  晴雨莳萝-----------------

    /**
     * 拍照，以originName命名，可以调用getPhoto得到时间命名的file照片。
     *
     * @param originName        拍照后保存的名字
     * @param isNoFaceDetection 是否开启人脸识别拍照
     */
    public void capture(@NonNull String originName, @NonNull boolean isNoFaceDetection) {
        if (hasCamera()) {
            //android6.0以上实现StrictMode API政策禁
            //android6.0以上实现StrictMode API政策禁
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionUtils.lacksPermissions(PERMISSIONS)) {
//                    permissionUtils.requestPermission(PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
//                    permissionUtils.requestPermission(PermissionUtils.CODE_CAMERA, mPermissionGrant);
                    final String permission = Manifest.permission.CAMERA;  //相机权限
                    permissionUtils.requestPermissions(PERMISSIONS, CAMERA_REQUEST_PERMISSION_CODE, permission);
                } else {
                    startCamera(originName, isNoFaceDetection);
                }
            } else {
                startCamera(originName, isNoFaceDetection);//低版本则不用考虑那么多
            }
        } else {
            Toast.makeText(mActivity, "没有检测到相机", Toast.LENGTH_SHORT).show();
        }
    }
 /*
    *
    * 私有方法*/

    /**
     * 打开相机获取图片
     */
    private void startCamera(@NonNull String originName, @NonNull boolean isNoFaceDetection) {
        createRootFile();
        createPhotoFile(originName);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", isNoFaceDetection);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //第二参数是在manifest.xml定义 provider的authorities属性
            mPhotoUri = FileProvider.getUriForFile(mActivity, "com.an.base.fileprovider.tackphoto", mPhotoFile);
        } else {
            mPhotoUri = Uri.fromFile(mPhotoFile);
        }
        //兼容版本处理，因为 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) 只在5.0以上的版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip = ClipData.newUri(mActivity.getContentResolver(), "A photo", mPhotoUri);
            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList = mActivity.getPackageManager()
                    .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                mActivity.grantUriPermission(packageName, mPhotoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        mActivity.startActivityForResult(intent, RESULT_CAPTURE_CODE);
    }

    /**
     * 创建照片文件-用户自定义文件类型
     */
    private void createPhotoFile(@NonNull String originName) {
        if (skRoot != null) {
            mPhotoFile = new File(skRoot, originName + ".jpg");
            //可能有问题，待解决（存在这个照片删除再创建新的。
            if (mPhotoFile.exists()) {
                mPhotoFile.delete();
            }//防止创建不了
            mPhotoFile = new File(skRoot, originName + ".jpg");
        } else {
            Toast.makeText(mActivity, "不存在该目录！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 裁剪照片-用户自定义文件类型（裁剪后图片保存cropName类型的图片）
     */
    public void startCaptureZoom(@NonNull String cropName) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        String cropPath = skRoot + File.separator + cropName + ".jpg";
        cropFile = new File(cropPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //通过FileProvider创建一个content类型的Uri
            mPhotoUri = FileProvider.getUriForFile(mActivity, "com.an.base.fileprovider.tackphoto", mPhotoFile);
            imageCropUri = FileProvider.getUriForFile(mActivity, "com.an.base.fileprovider.tackphoto", cropFile);
        } else {
            //原始图片的Uri
            mPhotoUri = Uri.fromFile(mPhotoFile);
            //裁剪后图片的Uri
            imageCropUri = Uri.fromFile(cropFile);
        }
        intent.setDataAndType(mPhotoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 700);
        intent.putExtra("outputY", 700);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        mActivity.startActivityForResult(intent, RESULT_CAPTURE_CROP_CODE);
    }

//    -----------------------分割线----------2017年4月1日 10:12:29  晴雨莳萝-----------------

    /**
     * 调用系统相册，并返回RESULT_PHOTO_CODE
     */
    public void photo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionUtils.lacksPermissions(PERMISSIONS)) {
//                permissionUtils.requestPermission(PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
//                permissionUtils.requestPermission(PermissionUtils.CODE_CAMERA, mPermissionGrant);
                final String permission = Manifest.permission.CAMERA;  //相机权限
                permissionUtils.requestPermissions(PERMISSIONS, PHOTO_REQUEST_PERMISSION_CODE, permission);
            } else {
                openAlbum();
            }
        } else {
            openAlbum();
        }
    }

    /*
    *
    * 私有方法*/
    private void openAlbum() {
        createRootFile();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        intent.setType("image/*");
        mActivity.startActivityForResult(intent, RESULT_PHOTO_CODE);
    }

    /*
    * 知识点讲解，裁剪这里对系统图片进行裁剪提供两种方式让我们的activity可以得到这张裁剪后的图片
    * ，startPhotoZoom有一个返回值，而且裁剪后图片也有返回码RESULT_PHOTO_CROP_CODE
    * */

    /**
     * 注意：（这里必须需要Intent的data参数。）
     * 裁剪照片保存为当前系统时间的照片--针对图库的裁剪。
     *
     * @param cropWidthSize  裁剪的宽度150
     * @param cropHeightSize 裁剪的高度150
     **/
    public void startPhotoZoom(@NonNull Intent data, int cropWidthSize, int cropHeightSize) {
        //默认
        Intent intent = new Intent("com.android.camera.action.CROP");
        String dataFormat = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
        String cropPath = skRoot + File.separator + dataFormat + ".jpg";
        cropFile = new File(cropPath);

        if (cropHeightSize < 150) {
            cropWidthSize = 150;
            cropHeightSize = 150;
        }
        if (data.getData() != null) {
            String url = getPath(mActivity, data.getData());
            mPhotoFile = new File(url);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //通过FileProvider创建一个content类型的Uri
            mPhotoUri = FileProvider.getUriForFile(mActivity, "com.an.base.fileprovider.tackphoto", mPhotoFile);
            imageCropUri = FileProvider.getUriForFile(mActivity, "com.an.base.fileprovider.tackphoto", cropFile);
        } else {
            //原始图片的Uri
            mPhotoUri = Uri.fromFile(mPhotoFile);
            //裁剪后图片的Uri
            imageCropUri = Uri.fromFile(cropFile);
        }
        intent.setDataAndType(mPhotoUri, "image/*");
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
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        mActivity.startActivityForResult(intent, RESULT_PHOTO_CROP_CODE);
    }

    /**
     * 注意：（这里必须需要Intent的data参数。）
     * 裁剪照片保存为当前cropName对图库的裁剪。
     *
     * @param cropName       裁剪并保存名为cropName的jpg照片
     * @param cropWidthSize  裁剪的宽度
     * @param cropHeightSize 裁剪的高度400*400
     */
    public void startPhotoZoom(@NonNull Intent data, @NonNull String cropName, @NonNull int cropWidthSize, @NonNull int cropHeightSize) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        String cropPath = skRoot + File.separator + cropName + ".jpg";
        cropFile = new File(cropPath);
        if (data.getData() != null) {
            String url = getPath(mActivity, data.getData());
            mPhotoFile = new File(url);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            mPhotoUri = FileProvider.getUriForFile(mActivity, "com.an.base.fileprovider.tackphoto", mPhotoFile);
            imageCropUri = FileProvider.getUriForFile(mActivity, "com.an.base.fileprovider.tackphoto", cropFile);
        } else {
            //原始图片的Uri
            mPhotoUri = Uri.fromFile(mPhotoFile);
            //裁剪后图片的Uri
            imageCropUri = Uri.fromFile(cropFile);
        }
        //默认
        if (cropHeightSize < 150) {
            cropWidthSize = 150;
            cropHeightSize = 150;
        }

        intent.setDataAndType(mPhotoUri, "image/*");
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


//    -----------------------分割线----------2017年4月1日 10:12:29  晴雨莳萝-----------------

    /**
     * 创建照片文件-用户自定义文件类型
     * 。创建文件没有问题，主要是因为Manifest.xml中有问题。
     */
    private File createResultFile(@NonNull String originName) {
        createRootFile();
        if (skRoot != null) {
            resultFile = new File(skRoot, originName + ".jpg");
            if (resultFile.exists()) {
                resultFile.delete();
            }
            resultFile = new File(skRoot, originName + ".jpg");
            if (!resultFile.exists())
                resultFile.mkdirs();

        } else {
            Toast.makeText(mActivity, "不存在该目录！", Toast.LENGTH_SHORT).show();
        }
        return resultFile;
    }

    /**
     * @param mBitmap  bitmap
     * @param saveName 保存的名字
     * @ 该方法单独使用，保存名为saveName的照片到内存卡（不管是拍照后裁剪的照片，还是选择图库后的照片建议都保存起来，下次获取，这里保存不需要权限。）
     */
    public void saveBitmap(@NonNull Bitmap mBitmap, @NonNull String saveName) {
        createResultFile(saveName);
        if (resultFile.exists()) {
            resultFile.delete();
        }
        try {
            FileOutputStream fOut = new FileOutputStream(resultFile);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param data
     * @return 得到原尺寸的的Bitmap对象
     */

    public Bitmap getCompressBitmap(@NonNull Context context, @NonNull Intent data, String saveName) {
        mPhotoUri = data.getData();
        if (mPhotoUri != null) {
            InputStream inputStream = null;
            try {
                inputStream = context.getContentResolver().openInputStream(mPhotoUri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                BitmapFactory.decodeStream(inputStream, null, options);
                int originalWidth = options.outWidth;
                int originalHeight = options.outHeight;
                if (originalHeight == -1 || originalWidth == -1) return null;
                float height = 1280f;
                float width = 960f;
                int noCompress = 1;
                if (originalWidth > originalHeight && originalWidth > width) {
                    noCompress = (int) (originalWidth / width);
                } else if (originalWidth < originalHeight && originalHeight > height) {
                    noCompress = (int) (originalHeight / height);
                }
                if (noCompress <= 0) noCompress = 1;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inSampleSize = noCompress;
                options.inJustDecodeBounds = true;
                bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
                inputStream = context.getContentResolver().openInputStream(mPhotoUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, bitmapOptions);
                return CompressBitmap(context, bitmap, saveName);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
        return null;
    }

    /*
    * captureHelper中g根据名字得到保存后的bitmap对象
    * */
    public Bitmap getBitmap(@NonNull File skRoot, @NonNull String saveName) {
        Bitmap bitmap = null;
        resultFile = new File(skRoot, saveName + ".jpg");
        if (resultFile.exists()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //通过FileProvider创建一个content类型的Uri
                resultUri = FileProvider.getUriForFile(mActivity, "com.an.base.fileprovider.tackphoto", resultFile);
            } else {
                //原始图片的Uri
                resultUri = Uri.fromFile(resultFile);
                //低版本可以用该方法加载内存中的一张图片。
//                Bitmap bitmap = BitmapFactory.decodeFile(path);
//                iv.setImageBitmap(bitmap);
            }
            try {
                bitmap = BitmapFactory.decodeStream(mActivity.getContentResolver().openInputStream(resultUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /*
    * captureHelper中g根据名字得到保存后的bitmap对象
    * */
    public Bitmap getBitmap(@NonNull File absoluteFile) {
        Bitmap bitmap = null;
        if (absoluteFile == null)
            return null;
        if (absoluteFile.exists()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //通过FileProvider创建一个content类型的Uri
                resultUri = FileProvider.getUriForFile(mActivity, "com.an.base.fileprovider.tackphoto", absoluteFile);
            } else {
                //原始图片的Uri
                resultUri = Uri.fromFile(absoluteFile);
                //低版本可以用该方法加载内存中的一张图片。
//                Bitmap bitmap = BitmapFactory.decodeFile(path);
//                iv.setImageBitmap(bitmap);
            }
            try {
                bitmap = BitmapFactory.decodeStream(mActivity.getContentResolver().openInputStream(resultUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 压缩图片
     *
     * @param bitmap   需要压缩图片的bitmap对象
     * @param context  上下文
     * @param saveName 需要保存的名字 ，可以为空
     * @return
     */
    private Bitmap CompressBitmap(@NonNull Context context, @NonNull Bitmap bitmap, String saveName) throws IOException {
        int options = 100;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);  //先不压缩，数据保存到outputStream；//主要还是采用的则会个方法
        while (outputStream.toByteArray().length / 1024 > 350) {//循环判断如果压缩后图片是否大于400kb,大于继续压缩
            outputStream.reset(); //重置
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, null);
        //保存为resultFile的对象
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(createResultFile(saveName)));
            decodeStream.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return decodeStream;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public Bitmap rotateBitmapByDegree(Bitmap bm, int degree, String saveName) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        //保存saveName 或default 的图片
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(createResultFile(saveName)));
            returnBm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            try {
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return returnBm;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

//    -----------------------分割线----------2017年4月1日 10:12:29  晴雨莳萝-----------------

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


    //以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...(该方法是否替换)
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
