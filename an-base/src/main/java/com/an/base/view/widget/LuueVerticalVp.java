package com.an.base.view.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sunshuntao on 16/3/30.
 * email : staryumou@163.com
 */
public class LuueVerticalVp extends ViewPager {
    private float startX, startY;


    public LuueVerticalVp(Context context) {
        super(context);
        init();
    }

    public LuueVerticalVp(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //设置页面滑动时候的动画
        setPageTransformer(true, new VerticalPageTransformer());
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private class VerticalPageTransformer implements PageTransformer {

        @Override
        public void transformPage(View page, float position) {
            if (position < -1) {
                page.setAlpha(0);
            } else if (position <= 1) {
                page.setAlpha(1);

                page.setTranslationX(page.getWidth() * -position);

                float yPosition = position * page.getHeight();
                page.setTranslationY(yPosition);

            } else {
                page.setAlpha(0);
            }
        }
    }

    private MotionEvent swapXY(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();

        float newX = (event.getY() / height) * width;
        float newY = (event.getX() / width) * height;

        event.setLocation(newX, newY);

        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = super.onInterceptTouchEvent(swapXY(event));
        swapXY(event);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                if (Math.abs(moveY - startY) - Math.abs(moveX - startX) > 0) {
                    return true;
                }
                break;
        }
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = (event.getY() / getHeight()) * getWidth();
                startY = (event.getY() / getWidth()) * getHeight();
                break;
        }
        return super.onTouchEvent(swapXY(event));
    }
}

