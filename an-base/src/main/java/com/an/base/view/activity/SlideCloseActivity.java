package com.an.base.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

import com.an.base.R;
import com.an.base.utils.NetBroadcastReceiver;
import com.an.base.utils.NetBroadcastReceiverUtils;
import com.an.base.view.DUtilsActivity;
import com.an.base.view.widget.WSlideFrameLayout;

import org.xutils.x;

public abstract class SlideCloseActivity extends AppCompatActivity implements NetBroadcastReceiver.NetEvevt {
    protected static String TAG = "SuperActivity";
    protected SharedPreferences sp;
    public static NetBroadcastReceiver.NetEvevt evevt;//广播监听网络
    protected Context mContext;
    /**
     * 网络类型
     */
    private int netModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //xUtils3.0+配置，可放在application中
        x.Ext.init(getApplication());
//        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        // 设置标题栏MD5
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 注解的入口
//        ViewUtils.inject(this); //xutils3.0之前需要这么注解
        x.view().inject(this); //xutils3.0之前需要这么注解
        // 用于确定当前界面是属于哪个活动(Activity), 让新加入开发的人快速锁定所在的界面,不得擅自移除.
        TAG = getClass().getSimpleName();
        // 将其子activity添加到activity采集器
        DUtilsActivity.getInstance().addActivity(this);

        mContext = this;

        WSlideFrameLayout rootView = new WSlideFrameLayout(this);
        rootView.bindActivity(this);

        //初始化视图
        initView();
        //网络变化监听相关
        evevt = this;
        inspectNet();
    }

    // 覆盖以下方法,设置动画.
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.aim_common_right_in,
                R.anim.aim_common_left_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.aim_common_right_in,
                R.anim.aim_common_zoom_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.aim_common_left_in,
                R.anim.aim_common_right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DUtilsActivity.getInstance().removeActivity(this);
    }

    /**
     * 初始化时判断有没有网络
     */

    public boolean inspectNet() {
        this.netModel = NetBroadcastReceiverUtils.getNetworkState(SlideCloseActivity.this);
        return isNetConnect();
    }

    /**
     * 网络变化之后的类型
     */
    @Override
    public void onNetChange(int netModel) {
        // TODO Auto-generated method stub
        this.netModel = netModel;
        isNetConnect();
    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netModel == 1) {
            return true;
        } else if (netModel == 0) {
            return true;
        } else if (netModel == -1) {
            return false;
        }
        return false;
    }

    /**
     * 简化Toast。
     *
     * @return null.
     */
    protected void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /**
     * 各种对象、组件的初始化
     */
    public abstract void initView();
}
