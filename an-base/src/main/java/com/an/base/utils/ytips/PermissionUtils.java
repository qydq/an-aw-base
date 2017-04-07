package com.an.base.utils.ytips;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.an.base.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qianxiaoai on 2016/7/7.
 */
public class PermissionUtils {

    private static final String TAG = PermissionUtils.class.getSimpleName();
    public static final int CODE_RECORD_AUDIO = 0;
    public static final int CODE_GET_ACCOUNTS = 1;
    public static final int CODE_READ_PHONE_STATE = 2;
    public static final int CODE_CALL_PHONE = 3;
    public static final int CODE_CAMERA = 4;
    public static final int CODE_ACCESS_FINE_LOCATION = 5;
    public static final int CODE_ACCESS_COARSE_LOCATION = 6;
    public static final int CODE_READ_EXTERNAL_STORAGE = 7;
    public static final int CODE_WRITE_EXTERNAL_STORAGE = 8;
    public static final int CODE_MULTI_PERMISSION = 100;

    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public static final String[] requestPermissions = {
            PERMISSION_RECORD_AUDIO,
            PERMISSION_GET_ACCOUNTS,
            PERMISSION_READ_PHONE_STATE,
            PERMISSION_CALL_PHONE,
            PERMISSION_CAMERA,
            PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_ACCESS_COARSE_LOCATION,
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_WRITE_EXTERNAL_STORAGE
    };

    private Context mContext;
    private Activity mActivity;

    public interface PermissionGrant {
        void onPermissionGranted(int requestCode);
    }

    public PermissionUtils(Activity activity, Context context) {
        mContext = context.getApplicationContext();
        mActivity = activity;
    }

    /**
     * Requests permission.
     *
     * @param requestCode request code, e.g. if you need request CAMERA permission,parameters is PermissionUtils.CODE_CAMERA
     */
    public void requestPermission(final int requestCode, PermissionGrant permissionGrant) {
        if (mActivity == null) {
            return;
        }

        Log.i(TAG, "requestPermission requestCode:" + requestCode);
        if (requestCode < 0 || requestCode >= requestPermissions.length) {
            Log.w(TAG, "requestPermission illegal requestCode:" + requestCode);
            return;
        }

        final String requestPermission = requestPermissions[requestCode];

        //如果是6.0以下的手机，ActivityCompat.checkSelfPermission()会始终等于PERMISSION_GRANTED，
        // 但是，如果用户关闭了你申请的权限，ActivityCompat.checkSelfPermission(),会导致程序崩溃(java.lang.RuntimeException: Unknown exception code: 1 msg null)，
        // 你可以使用try{}catch(){},处理异常，也可以在这个地方，低于23就什么都不做，
        // 个人建议try{}catch(){}单独处理，提示用户开启权限。
//        if (Build.VERSION.SDK_INT < 23) {
//            return;
//        }

        int checkSelfPermission;
        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission(mActivity, requestPermission);
        } catch (RuntimeException e) {
            Toast.makeText(mActivity, "please open this permission", Toast.LENGTH_SHORT)
                    .show();
            Log.e(TAG, "RuntimeException:" + e.getMessage());
            return;
        }

        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED");


            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, requestPermission)) {
                Log.i(TAG, "requestPermission shouldShowRequestPermissionRationale");
                shouldShowRationale(requestCode, requestPermission);

            } else {
                Log.d(TAG, "requestCameraPermission else");
                ActivityCompat.requestPermissions(mActivity, new String[]{requestPermission}, requestCode);
            }

        } else {
            Log.d(TAG, "ActivityCompat.checkSelfPermission ==== PackageManager.PERMISSION_GRANTED");
            Toast.makeText(mActivity, "opened:" + requestPermissions[requestCode], Toast.LENGTH_SHORT).show();
            permissionGrant.onPermissionGranted(requestCode);
        }
    }

    private void requestMultiResult(String[] permissions, int[] grantResults, PermissionGrant permissionGrant) {

        if (mActivity == null) {
            return;
        }

        //TODO
        Log.d(TAG, "onRequestPermissionsResult permissions length:" + permissions.length);
        Map<String, Integer> perms = new HashMap<>();

        ArrayList<String> notGranted = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            Log.d(TAG, "permissions: [i]:" + i + ", permissions[i]" + permissions[i] + ",grantResults[i]:" + grantResults[i]);
            perms.put(permissions[i], grantResults[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(permissions[i]);
            }
        }

        if (notGranted.size() == 0) {
            Toast.makeText(mActivity, "all permission success" + notGranted, Toast.LENGTH_SHORT)
                    .show();
            permissionGrant.onPermissionGranted(CODE_MULTI_PERMISSION);
        } else {
            openSettingActivity("those permission need granted!");
        }

    }


    /**
     * 一次申请多个权限
     */
    public void requestMultiPermissions(PermissionGrant grant) {

        final List<String> permissionsList = getNoGrantedPermission(mActivity, false);
        final List<String> shouldRationalePermissionsList = getNoGrantedPermission(mActivity, true);

        //TODO checkSelfPermission
        if (permissionsList == null || shouldRationalePermissionsList == null) {
            return;
        }
        Log.d(TAG, "requestMultiPermissions permissionsList:" + permissionsList.size() + ",shouldRationalePermissionsList:" + shouldRationalePermissionsList.size());

        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions(mActivity, permissionsList.toArray(new String[permissionsList.size()]),
                    CODE_MULTI_PERMISSION);
            Log.d(TAG, "showMessageOKCancel requestPermissions");

        } else if (shouldRationalePermissionsList.size() > 0) {
            showMessageOKCancel(mActivity, "should open those permission",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(mActivity, shouldRationalePermissionsList.toArray(new String[shouldRationalePermissionsList.size()]),
                                    CODE_MULTI_PERMISSION);
                            Log.d(TAG, "showMessageOKCancel requestPermissions");
                        }
                    });
        } else {
            grant.onPermissionGranted(CODE_MULTI_PERMISSION);
        }

    }


    private void shouldShowRationale(final int requestCode, final String requestPermission) {
        //TODO
        String[] permissionsHint = mActivity.getResources().getStringArray(R.array.permissions);
        showMessageOKCancel(mActivity, "Rationale: " + permissionsHint[requestCode], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions((Activity) mActivity,
                        new String[]{requestPermission},
                        requestCode);
                Log.d(TAG, "showMessageOKCancel requestPermissions:" + requestPermission);
            }
        });
    }

    private void showMessageOKCancel(final Activity context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

    /**
     * @param requestCode  Need consistent with requestPermission
     * @param permissions
     * @param grantResults
     */
    public void requestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults, PermissionGrant permissionGrant) {

        if (mActivity == null) {
            return;
        }
        Log.d(TAG, "requestPermissionsResult requestCode:" + requestCode);

        if (requestCode == CODE_MULTI_PERMISSION) {
            requestMultiResult(permissions, grantResults, permissionGrant);
            return;
        }

        if (requestCode < 0 || requestCode >= requestPermissions.length) {
            Log.w(TAG, "requestPermissionsResult illegal requestCode:" + requestCode);
            Toast.makeText(mActivity, "illegal requestCode:" + requestCode, Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG, "onRequestPermissionsResult requestCode:" + requestCode + ",permissions:" + permissions.toString()
                + ",grantResults:" + grantResults.toString() + ",length:" + grantResults.length);

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onRequestPermissionsResult PERMISSION_GRANTED");
            //TODO success, do something, can use callback
            permissionGrant.onPermissionGranted(requestCode);

        } else {
            //TODO hint user this permission function
            Log.i(TAG, "onRequestPermissionsResult PERMISSION NOT GRANTED");
            //TODO
            String[] permissionsHint = mActivity.getResources().getStringArray(R.array.permissions);
            openSettingActivity("Result" + permissionsHint[requestCode]);
        }

    }

    private void openSettingActivity(String message) {

        showMessageOKCancel(mActivity, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Log.d(TAG, "getPackageName(): " + mActivity.getPackageName());
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                intent.setData(uri);
                mActivity.startActivity(intent);
            }
        });
    }


    /**
     * @param activity
     * @param isShouldRationale true: return no granted and shouldShowRequestPermissionRationale permissions, false:return no granted and !shouldShowRequestPermissionRationale
     * @return
     */
    public ArrayList<String> getNoGrantedPermission(Activity activity, boolean isShouldRationale) {

        ArrayList<String> permissions = new ArrayList<>();

        for (int i = 0; i < requestPermissions.length; i++) {
            String requestPermission = requestPermissions[i];


            //TODO checkSelfPermission
            int checkSelfPermission = -1;
            try {
                checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
            } catch (RuntimeException e) {
                Toast.makeText(activity, "please open those permission", Toast.LENGTH_SHORT)
                        .show();
                Log.e(TAG, "RuntimeException:" + e.getMessage());
                return null;
            }

            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "getNoGrantedPermission ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED:" + requestPermission);

                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {
                    Log.d(TAG, "shouldShowRequestPermissionRationale if");
                    if (isShouldRationale) {
                        permissions.add(requestPermission);
                    }

                } else {

                    if (!isShouldRationale) {
                        permissions.add(requestPermission);
                    }
                    Log.d(TAG, "shouldShowRequestPermissionRationale else");
                }

            }
        }

        return permissions;
    }

    // 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    public void requestCapturePermissions(String[] PERMISSIONS, int requestCode, String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {  //给出权限申请说明
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS, requestCode);
        } else { //直接申请权限
            //申请权限，可同时申请多个权限，并根据用户是否赋予权限进行判断
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS, requestCode);
        }
    }

}