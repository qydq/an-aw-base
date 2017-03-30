package com.an.base.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import org.xutils.x;

import static com.an.base.AnApplication.AnTAG;

/**
 * Created by stary on 2016/8/18.
 * 文件名称：BaseFragment
 * 文件描述：
 * 作者：staryumou@163.com
 * 修改时间：2016/8/18
 */

public abstract class BaseFragment extends Fragment implements OnTouchListener {

    /**
     * 子类默认使用的日志输出标签
     */
    protected String TAG = "";

    protected Context mContext = null;
    protected View view;
    protected SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(getLayoutId(), null);
        this.mContext = inflater.getContext();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            sp = mContext.getSharedPreferences(AnTAG, Context.MODE_PRIVATE);
        }
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        }
//可以解封，需引用ViewUtils这是注解部分。
//        ViewUtils.inject(this, view);
        x.view().inject(this, inflater, container);
        init();
        TAG = BaseFragment.class.getSimpleName();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.setOnTouchListener(this);// 拦截触摸事件，防止内存泄露下去
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 拦截触摸事件，防止内存泄露下去
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    public abstract int getLayoutId();

    public abstract void init();

}

