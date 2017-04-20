package com.an.base.view.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by qydda on 2016/12/21.
 */

public class YresizeRelativeLayout extends RelativeLayout {

    public static final int HIDE = 0;
    public static final int SHOW = 1;

    private Handler mainHandler = new Handler();

    public YresizeRelativeLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public YresizeRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onSizeChanged(int w, final int h, int oldw, final int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (oldh - h > 48) {
                    keyBordStateListener.onStateChange(SHOW);
                } else {
                    if (keyBordStateListener != null) {
                        keyBordStateListener.onStateChange(HIDE);
                    }
                }
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private KeyBordStateListener keyBordStateListener;

    public void setKeyBordStateListener(KeyBordStateListener keyBordStateListener) {
        this.keyBordStateListener = keyBordStateListener;
    }



    public interface KeyBordStateListener {
        public void onStateChange(int state);
    }
}