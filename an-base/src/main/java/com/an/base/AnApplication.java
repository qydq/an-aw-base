package com.an.base;


import android.app.Application;

import org.xutils.x;

/**
 * Created by stary on 2016/11/25.
 * 莳萝花，晴雨荡气，sunshuntao，qydq
 * Contact : qyddai@gmail.com
 * 说明：null
 * 最后修改：on 2016/11/25.
 */

public class AnApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
