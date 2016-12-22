/**
 * @Title: RoundImageView.java
 * @Package com.yanzhi.healthcare.view
 * @Description: TODO(用一句话描述该文件做什么)
 * @author wangkun
 * @date 2014-9-2 下午6:40:37
 */
package com.an.base.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @ClassName: WRoundImageView
 * @Description: TODO 圆形图片
 * @date 2014-9-2 下午6:40:37
 */

/**
 * 圆形ImageView，可设置最多两个宽度不同且颜色不同的圆形边框。
 *
 * @author Alan
 */
public class WRoundImageView extends ImageView {
    private int mBorderThickness = 0;
    private Context mContext;
    private int defaultColor = 0xFFFFFFFF;

    // 控件默认长、宽
    private int defaultWidth = 0;
    private int defaultHeight = 0;

    public WRoundImageView(Context context) {
        super(context);
        mContext = context;
    }

    public WRoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public WRoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        try {
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }

            if (getWidth() == 0 || getHeight() == 0) {
                return;
            }
            this.measure(0, 0);
            if (drawable.getClass() == NinePatchDrawable.class)
                return;
            Bitmap b = ((BitmapDrawable) drawable).getBitmap();
            Bitmap bitmap = b.copy(Config.RGB_565, true);
            if (defaultWidth == 0) {
                defaultWidth = getWidth();

            }
            if (defaultHeight == 0) {
                defaultHeight = getHeight();
            }
            // 保证重新读取图片后不会因为图片大小而改变控件宽、高的大小（针对宽、高为wrap_content布局的imageview，但会导致margin无效）
            // if (defaultWidth != 0 && defaultHeight != 0) {
            // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            // defaultWidth, defaultHeight);
            // setLayoutParams(params);
            // }
            int radius = (defaultWidth < defaultHeight ? defaultWidth
                    : defaultHeight) / 2;

            Bitmap roundBitmap = getCroppedRoundBitmap(bitmap, radius);
            canvas.drawBitmap(roundBitmap, defaultWidth / 2 - radius, defaultHeight
                    / 2 - radius, null);
        } catch (Exception e) {

        }
    }

    /**
     * @param bmp
     * @param radius
     * @return获取裁剪后的圆形图片
     */
    public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
        Bitmap scaledSrcBmp;
        int diameter = radius * 2;

        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int squareWidth = 0, squareHeight = 0;
        int x = 0, y = 0;
        Bitmap squareBitmap;
        if (bmpHeight > bmpWidth) {// 高大于宽
            squareWidth = squareHeight = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth) / 2;
            // 截取正方形图片
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else if (bmpHeight < bmpWidth) {// 宽大于高
            squareWidth = squareHeight = bmpHeight;
            x = (bmpWidth - bmpHeight) / 2;
            y = 0;
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else {
            squareBitmap = bmp;
        }

        if (squareBitmap.getWidth() != diameter
                || squareBitmap.getHeight() != diameter) {
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
                    diameter, true);

        } else {
            scaledSrcBmp = squareBitmap;
        }
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
                scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
                paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
        return output;
    }
}
