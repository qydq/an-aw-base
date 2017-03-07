package com.qyddai.an_aw_base.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by qydda on 2017/3/2.
 */

public class ScalView extends View {
    private Paint mPaint = new Paint();
    private int mWidth;
    private int mHeight;

    private void initPaint() {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10f);
    }

    public ScalView(Context context) {
        super(context);
        initPaint();
    }

    public ScalView(Context context, AttributeSet attrs) {
        super(context);
        initPaint();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);
        RectF rectF = new RectF(-400, -400, 400, 400);
        for (int i = 0; i <= 20; i++) {
            canvas.scale(0.9f, 0.9f);
            canvas.drawRect(rectF, mPaint);
        }
    }
}
