package com.an.base.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.an.base.utils.AndroidTranslucentBar;
import com.an.base.utils.NetBroadcastReceiver;
import com.an.base.utils.NetBroadcastReceiverUtils;
import com.an.base.view.BaseActivity;
import com.an.base.view.widget.WSwipeCloseFrameLayout;

public abstract class SwipeCloseActivity extends BaseActivity implements NetBroadcastReceiver.NetEvevt {
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
        //环信集成功能，暂未开启。
//		EMChat.getInstance().init(this.getApplicationContext());
        mContext = this;
        //an框架的夜间模式。用来保存皮肤切换模式的sp
        sp = this.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        Window window = getWindow();
        AndroidTranslucentBar.getInstance().setTranslucentBar(window);
        //SwipeCloseActivity init
        WSwipeCloseFrameLayout rootView = new WSwipeCloseFrameLayout(this);
        rootView.bindActivity(this);
        //初始化视图
        initView();
        //网络变化监听相关
        evevt = this;
        inspectNet();
    }

    /**
     * 初始化时判断有没有网络
     */

    public boolean inspectNet() {
        this.netModel = NetBroadcastReceiverUtils.getNetworkState(SwipeCloseActivity.this);
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
