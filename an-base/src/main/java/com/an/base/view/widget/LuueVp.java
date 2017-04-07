package com.an.base.view.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 此ViewPager解决与父容器ScrollView冲突的问题,无法完美解决.有卡顿 此自定义组件和下拉刷新scrollview配合暂时小完美，有待改善
 *
 * @author 孙顺涛 ,em: staryumou@163.com
 */

public class LuueVp extends ViewPager {
    float curX = 0f;
    float downX = 0f;
    OnSingleTouchListener onSingleTouchListener;

    public LuueVp(Context context) {
        // TODO Auto-generated constructor stub
        super(context);
    }

    public LuueVp(Context context, AttributeSet attrs) {
        // TODO Auto-generated constructor stub
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        curX = ev.getX();
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = curX;
        }
        int curIndex = getCurrentItem();
        if (curIndex == 0) {
            if (downX <= curX) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        } else if (curIndex == getAdapter().getCount() - 1) {
            if (downX >= curX) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        } else {
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        return super.onTouchEvent(ev);
    }

    public void onSingleTouch() {
        if (onSingleTouchListener != null) {
            onSingleTouchListener.onSingleTouch();
        }
    }

    public interface OnSingleTouchListener {
        public void onSingleTouch();
    }

    public void setOnSingleTouchListner(
            OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return super.onInterceptTouchEvent(arg0);
    }

}
