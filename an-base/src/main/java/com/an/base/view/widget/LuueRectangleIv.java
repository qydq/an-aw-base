package com.an.base.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.an.base.R;

/*
* 两边举行圆角试图view
* */
public class LuueRectangleIv extends ImageView {
    private Paint paint;
    private Bitmap sbmp;
    private float left;
    private float top;
    private float right;
    private float bottom;

    public LuueRectangleIv(Context context) {
        this(context, null);

    }

    public LuueRectangleIv(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public LuueRectangleIv(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
    }

    protected void onDraw(Canvas canvas) {
        int roundPx = 20;//圆角的大小，这个单位是像素
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.ColorTransparent)); //这里的颜色决定了边缘的颜色
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        if (drawable instanceof BitmapDrawable) {

            Bitmap b = ((BitmapDrawable) drawable).getBitmap();
            Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
            int w = getWidth();
            int h = getHeight();
            RectF rectF = new RectF(0, 0, w, h);

            Bitmap roundBitmap = getCroppedBitmap(bitmap, w, h, roundPx);

            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            canvas.drawBitmap(roundBitmap, 0, 0, null);
        }

    }

    public Bitmap getCroppedBitmap(Bitmap bmp, int lengthx, int lengthy, int roundPx) {

        if (bmp.getWidth() != lengthx || bmp.getHeight() != lengthy)
            sbmp = Bitmap.createScaledBitmap(bmp, lengthx, lengthy, false);
        else
            sbmp = bmp;

        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        //left、right -:向右 +:向左   top、bottom -:向下 +:向上
        final RectF rectF = new RectF(left, top, sbmp.getWidth() + right, sbmp.getHeight() + bottom);
//        final RectF rectF = new RectF(-3, 0, sbmp.getWidth()+3, sbmp.getHeight() - 2);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

    //这个方法是通过外部来控制边框的大小
    public void setRectFSize(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

}