package com.qyddai.an_aw_base;

import android.app.Application;

import com.an.base.AnApplication;
import com.qyddai.an_aw_base.view.Engine;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/6/21 下午10:13
 * 描述:
 */
public class App extends AnApplication {
    private static App sInstance;
    private Engine mEngine;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mEngine = new Retrofit.Builder()
                .baseUrl("http://7xk9dj.com1.z0.glb.clouddn.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Engine.class);
    }

    public static App getInstance() {
        return sInstance;
    }

    public Engine getEngine() {
        return mEngine;
    }

}