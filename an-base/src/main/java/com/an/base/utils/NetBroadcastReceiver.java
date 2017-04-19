package com.an.base.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.an.base.view.SuperActivity;


/**********************************************************
 * @文件名称：NetBroadcastReceiver
 * @文件作者：staryumou@163.com
 * @创建时间：2016/9/14
 * @文件描述：自定义检查手机网络状态是否切换的广播接受器
 * @修改历史：2016/9/14
 **********************************************************/


/*记得在manifest中注册
<receiver android:name="cn.broadcastreceiver.NetBroadcastReceiver">
<intent-filter>
<action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
</intent-filter>
</receiver>*/

public class NetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NetEvevt evevt = SuperActivity.evevt;
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWrokState = NetBroadcastReceiverUtils.getNetworkState(context);
            // 接口回调传过去状态的类型
            if (evevt != null)
                evevt.onNetChange(netWrokState);
        }
    }

    // 自定义接口
    public interface NetEvevt {
        void onNetChange(int netModile);
    }
}
