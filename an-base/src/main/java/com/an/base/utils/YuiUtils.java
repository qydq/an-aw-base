package com.an.base.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Luue on 2016/6/18。
 */

public class YuiUtils {

    private volatile static YuiUtils instance;

    private YuiUtils() {

    }

    //线程安全机制。
    public static YuiUtils getInstance() {
        if (instance == null) {
            synchronized (YuiUtils.class) {
                if (instance == null)
                    instance = new YuiUtils();
            }
        }
        return instance;
    }

    /**
     * sunshuntao ;staryumou@163.com
     * 图片跳动 按钮模拟心脏跳动
     */
    public void playHeartbeatAnimation(final View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f));
        animationSet.setDuration(200);
        animationSet.setInterpolator(new AccelerateInterpolator());
        // 结尾停在最后一针
        animationSet.setFillAfter(true);
        // 对动画进行监听
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            // 开始时候怎么样
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            // 结束时候怎么样
            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(new ScaleAnimation(1.2f, 1.0f, 1.2f,
                        1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f));
                animationSet.setDuration(200);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setFillAfter(false);
                // 实现心跳的View
                view.startAnimation(animationSet);
            }
        });
        // 实现心跳的View
        view.startAnimation(animationSet);
    }

    /*
    * 软键盘的操作 待加入
    * */
     /*
    * 隐藏软键盘
    * */
    public void hindKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public boolean isOpenKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

    /**
     * 判断软键盘是否弹出
     */
    public boolean isSHowKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        if (imm.hideSoftInputFromWindow(v.getWindowToken(), 0)) {
            imm.showSoftInput(v, 0);
            return true;
            //软键盘已弹出
        } else {
            return false;
            //软键盘未弹出
        }
    }
}
