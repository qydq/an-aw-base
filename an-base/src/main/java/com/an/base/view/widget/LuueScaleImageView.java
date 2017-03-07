package com.an.base.view.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/****
 * 这里你要明白几个方法执行的流程： 首先ImageView是继承自View的子类.
 * onLayout方法：是一个回调方法.该方法会在在View中的layout方法中执行，在执行layout方法前面会首先执行setFrame方法.
 * layout方法：
 * setFrame方法：判断我们的View是否发生变化，如果发生变化，那么将最新的l，t，r，b传递给View，然后刷新进行动态更新UI.
 * 并且返回ture.没有变化返回false.
 * invalidate方法：用于刷新当前控件,
 *
 * @author sunshuntao
 */
public class LuueScaleImageView extends ImageView {

    private Activity mActivity;

    private int screen_W, screen_H;// 可见屏幕的宽高度

    private int bitmap_W, bitmap_H;// 当前图片宽高

    private int MAX_W, MAX_H, MIN_W, MIN_H;// 极限值

    private int current_Top, current_Right, current_Bottom, current_Left;// 当前图片上下左右坐标

    private int start_Top = -1, start_Right = -1, start_Bottom = -1,
            start_Left = -1;// 初始化默认位置.

    private int start_x, start_y, current_x, current_y;// 触摸位置

    private float beforeLenght, afterLenght;// 两触点距离

    private float scale_temp;// 缩放比例

    /**
     * 模式 NONE：无 DRAG：拖拽. ZOOM:缩放
     *
     * @author zhangjia
     */
    private enum MODE {
        NONE, DRAG, ZOOM
    }

    private MODE mode = MODE.NONE;// 默认模式

    private boolean isControl_V = false;// 垂直监控

    private boolean isControl_H = false;// 水平监控

    private ScaleAnimation scaleAnimation;// 缩放动画

    private boolean isScaleAnim = false;// 缩放动画

    private MyAsyncTask myAsyncTask;// 异步动画

    /**
     * 构造方法
     **/
    public LuueScaleImageView(Context context) {
        super(context);
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    /**
     * 可见屏幕宽度
     **/
    public void setScreen_W(int screen_W) {
        this.screen_W = screen_W;
    }

    /**
     * 可见屏幕高度
     **/
    public void setScreen_H(int screen_H) {
        this.screen_H = screen_H;
    }

    public LuueScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        MatrixTouchListener mListener = new MatrixTouchListener();
        setOnTouchListener(mListener);
        mGestureDetector = new GestureDetector(getContext(), new GestureListener(mListener));
        //背景设置为balck
        setBackgroundColor(Color.BLACK);
        //将缩放类型设置为FIT_CENTER，表示把图片按比例扩大/缩小到View的宽度，居中显示
        setScaleType(ScaleType.FIT_CENTER);

    }

    /***
     * 设置显示图片
     */
    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        /** 获取图片宽高 **/
        bitmap_W = bm.getWidth();
        bitmap_H = bm.getHeight();

        MAX_W = bitmap_W * 3;
        MAX_H = bitmap_H * 3;

        MIN_W = bitmap_W / 2;
        MIN_H = bitmap_H / 2;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (start_Top == -1) {
            start_Top = top;
            start_Left = left;
            start_Bottom = bottom;
            start_Right = right;
        }

    }

    /***
     * touch 事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /** 处理单点、多点触摸 **/
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(event);
                break;
            // 多点触摸
            case MotionEvent.ACTION_POINTER_DOWN:
                onPointerDown(event);
                break;

            case MotionEvent.ACTION_MOVE:
                onTouchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                mode = MODE.NONE;
                break;

            // 多点松开
            case MotionEvent.ACTION_POINTER_UP:
                mode = MODE.NONE;
                /** 执行缩放还原 **/
                if (isScaleAnim) {
                    doScaleAnim();
                }
                break;
        }

        return true;
    }

    /**
     * 按下
     **/
    void onTouchDown(MotionEvent event) {
        mode = MODE.DRAG;
        current_x = (int) event.getRawX();
        current_y = (int) event.getRawY();
        start_x = (int) event.getX();
        start_y = current_y - this.getTop();

    }

    /**
     * 两个手指 只能放大缩小
     **/
    void onPointerDown(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            mode = MODE.ZOOM;
            beforeLenght = getDistance(event);// 获取两点的距离
        }
    }

    /**
     * 移动的处理
     **/
    void onTouchMove(MotionEvent event) {
        int left = 0, top = 0, right = 0, bottom = 0;
        /** 处理拖动 **/
        if (mode == MODE.DRAG) {

            /** 在这里要进行判断处理，防止在drag时候越界 **/

            /** 获取相应的l，t,r ,b **/
            left = current_x - start_x;
            right = current_x + this.getWidth() - start_x;
            top = current_y - start_y;
            bottom = current_y - start_y + this.getHeight();

            /** 水平进行判断 **/
            if (isControl_H) {
                if (left >= 0) {
                    left = 0;
                    right = this.getWidth();
                }
                if (right <= screen_W) {
                    left = screen_W - this.getWidth();
                    right = screen_W;
                }
            } else {
                left = this.getLeft();
                right = this.getRight();
            }
            /** 垂直判断 **/
            if (isControl_V) {
                if (top >= 0) {
                    top = 0;
                    bottom = this.getHeight();
                }

                if (bottom <= screen_H) {
                    top = screen_H - this.getHeight();
                    bottom = screen_H;
                }
            } else {
                top = this.getTop();
                bottom = this.getBottom();
            }
            if (isControl_H || isControl_V)
                this.setPosition(left, top, right, bottom);

            current_x = (int) event.getRawX();
            current_y = (int) event.getRawY();

        }
        /** 处理缩放 **/
        else if (mode == MODE.ZOOM) {

            afterLenght = getDistance(event);// 获取两点的距离

            float gapLenght = afterLenght - beforeLenght;// 变化的长度
            if (Math.abs(gapLenght) > 5f) {
                scale_temp = afterLenght / beforeLenght;// 求的缩放的比例
                this.setScale(scale_temp);
                beforeLenght = afterLenght;
            }
        }

    }

    /**
     * 获取两点的距离
     **/
    float getDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 实现处理拖动
     **/
    private void setPosition(int left, int top, int right, int bottom) {
        this.layout(left, top, right, bottom);
    }

    /**
     * 处理缩放
     **/
    void setScale(float scale) {
        int disX = (int) (this.getWidth() * Math.abs(1 - scale)) / 4;// 获取缩放水平距离
        int disY = (int) (this.getHeight() * Math.abs(1 - scale)) / 4;// 获取缩放垂直距离

        // 放大
        if (scale > 1 && this.getWidth() <= MAX_W) {
            current_Left = this.getLeft() - disX;
            current_Top = this.getTop() - disY;
            current_Right = this.getRight() + disX;
            current_Bottom = this.getBottom() + disY;

            this.setFrame(current_Left, current_Top, current_Right,
                    current_Bottom);
            /***
             * 此时因为考虑到对称，所以只做一遍判断就可以了。
             */
            if (current_Top <= 0 && current_Bottom >= screen_H) {
                //		Log.e("jj", "屏幕高度=" + this.getHeight());
                isControl_V = true;// 开启垂直监控
            } else {
                isControl_V = false;
            }
            if (current_Left <= 0 && current_Right >= screen_W) {
                isControl_H = true;// 开启水平监控
            } else {
                isControl_H = false;
            }
        }
        // 缩小
        else if (scale < 1 && this.getWidth() >= MIN_W) {
            current_Left = this.getLeft() + disX;
            current_Top = this.getTop() + disY;
            current_Right = this.getRight() - disX;
            current_Bottom = this.getBottom() - disY;
            /***
             * 在这里要进行缩放处理
             */
            // 上边越界
            if (isControl_V && current_Top > 0) {
                current_Top = 0;
                current_Bottom = this.getBottom() - 2 * disY;
                if (current_Bottom < screen_H) {
                    current_Bottom = screen_H;
                    isControl_V = false;// 关闭垂直监听
                }
            }
            // 下边越界
            if (isControl_V && current_Bottom < screen_H) {
                current_Bottom = screen_H;
                current_Top = this.getTop() + 2 * disY;
                if (current_Top > 0) {
                    current_Top = 0;
                    isControl_V = false;// 关闭垂直监听
                }
            }

            // 左边越界
            if (isControl_H && current_Left >= 0) {
                current_Left = 0;
                current_Right = this.getRight() - 2 * disX;
                if (current_Right <= screen_W) {
                    current_Right = screen_W;
                    isControl_H = false;// 关闭
                }
            }
            // 右边越界
            if (isControl_H && current_Right <= screen_W) {
                current_Right = screen_W;
                current_Left = this.getLeft() + 2 * disX;
                if (current_Left >= 0) {
                    current_Left = 0;
                    isControl_H = false;// 关闭
                }
            }

            if (isControl_H || isControl_V) {
                this.setFrame(current_Left, current_Top, current_Right,
                        current_Bottom);
            } else {
                this.setFrame(current_Left, current_Top, current_Right,
                        current_Bottom);
                isScaleAnim = true;// 开启缩放动画
            }

        }

    }

    /***
     * 缩放动画处理
     */
    public void doScaleAnim() {
        myAsyncTask = new MyAsyncTask(screen_W, this.getWidth(),
                this.getHeight());
        myAsyncTask.setLTRB(this.getLeft(), this.getTop(), this.getRight(),
                this.getBottom());
        myAsyncTask.execute();
        isScaleAnim = false;// 关闭动画
    }

    /***
     * 回缩动画執行
     */
    class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
        private int screen_W, current_Width, current_Height;

        private int left, top, right, bottom;

        private float scale_WH;// 宽高的比例

        /**
         * 当前的位置属性
         **/
        public void setLTRB(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        private float STEP = 8f;// 步伐

        private float step_H, step_V;// 水平步伐，垂直步伐

        public MyAsyncTask(int screen_W, int current_Width, int current_Height) {
            super();
            this.screen_W = screen_W;
            this.current_Width = current_Width;
            this.current_Height = current_Height;
            scale_WH = (float) current_Height / current_Width;
            step_H = STEP;
            step_V = scale_WH * STEP;
        }

        @Override
        protected Void doInBackground(Void... params) {

            while (current_Width <= screen_W) {

                left -= step_H;
                top -= step_V;
                right += step_H;
                bottom += step_V;

                current_Width += 2 * step_H;

                left = Math.max(left, start_Left);
                top = Math.max(top, start_Top);
                right = Math.min(right, start_Right);
                bottom = Math.min(bottom, start_Bottom);
                Log.e("jj", "top=" + top + ",bottom=" + bottom + ",left=" + left + ",right=" + right);
                onProgressUpdate(new Integer[]{left, top, right, bottom});
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(final Integer... values) {
            super.onProgressUpdate(values);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setFrame(values[0], values[1], values[2], values[3]);
                }
            });
        }

    }

    private GestureDetector mGestureDetector;
    /**
     * 模板Matrix，用以初始化
     */
    private Matrix mMatrix = new Matrix();
    /**
     * 图片长度
     */
    private float mImageWidth;
    /**
     * 图片高度
     */
    private float mImageHeight;

    class MatrixTouchListener implements OnTouchListener {
        /**
         * 拖拉照片模式
         */
        private static final int MODE_DRAG = 1;
        /**
         * 放大缩小照片模式
         */
        private static final int MODE_ZOOM = 2;
        /**
         * 不支持Matrix
         */
        private static final int MODE_UNABLE = 3;
        /**
         * 最大缩放级别
         */
        float mMaxScale = 6;
        /**
         * 双击时的缩放级别
         */
        float mDobleClickScale = 2;
        private int mMode = 0;//
        /**
         * 缩放开始时的手指间距
         */
        private float mStartDis;
        /**
         * 当前Matrix
         */
        private Matrix mCurrentMatrix = new Matrix();

        /**
         * 用于记录开始时候的坐标位置
         */
        private PointF startPoint = new PointF();

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    //设置拖动模式
                    mMode = MODE_DRAG;
                    startPoint.set(event.getX(), event.getY());
                    isMatrixEnable();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    reSetMatrix();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mMode == MODE_ZOOM) {
                        setZoomMatrix(event);
                    } else if (mMode == MODE_DRAG) {
                        setDragMatrix(event);
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    if (mMode == MODE_UNABLE) return true;
                    mMode = MODE_ZOOM;
                    mStartDis = getDistance(event);
                    break;
                default:
                    break;
            }

            return mGestureDetector.onTouchEvent(event);
        }

        public void setDragMatrix(MotionEvent event) {
            if (isZoomChanged()) {
                float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
                float dy = event.getY() - startPoint.y; // 得到x轴的移动距离
                //避免和双击冲突,大于6f才算是拖动
                if (Math.sqrt(dx * dx + dy * dy) > 6f) {
                    startPoint.set(event.getX(), event.getY());
                    //在当前基础上移动
                    mCurrentMatrix.set(getImageMatrix());
                    float[] values = new float[9];
                    mCurrentMatrix.getValues(values);
                    dx = checkDxBound(values, dx);
                    dy = checkDyBound(values, dy);
                    mCurrentMatrix.postTranslate(dx, dy);
                    setImageMatrix(mCurrentMatrix);
                }
            }
        }

        /**
         * 判断缩放级别是否是改变过
         *
         * @return true表示非初始值, false表示初始值
         */
        private boolean isZoomChanged() {
            float[] values = new float[9];
            getImageMatrix().getValues(values);
            //获取当前X轴缩放级别
            float scale = values[Matrix.MSCALE_X];
            //获取模板的X轴缩放级别，两者做比较
            mMatrix.getValues(values);
            return scale != values[Matrix.MSCALE_X];
        }

        /**
         * 和当前矩阵对比，检验dy，使图像移动后不会超出ImageView边界
         *
         * @param values
         * @param dy
         * @return
         */
        private float checkDyBound(float[] values, float dy) {
            float height = getHeight();
            if (mImageHeight * values[Matrix.MSCALE_Y] < height)
                return 0;
            if (values[Matrix.MTRANS_Y] + dy > 0)
                dy = -values[Matrix.MTRANS_Y];
            else if (values[Matrix.MTRANS_Y] + dy < -(mImageHeight * values[Matrix.MSCALE_Y] - height))
                dy = -(mImageHeight * values[Matrix.MSCALE_Y] - height) - values[Matrix.MTRANS_Y];
            return dy;
        }

        /**
         * 和当前矩阵对比，检验dx，使图像移动后不会超出ImageView边界
         *
         * @param values
         * @param dx
         * @return
         */
        private float checkDxBound(float[] values, float dx) {
            float width = getWidth();
            if (mImageWidth * values[Matrix.MSCALE_X] < width)
                return 0;
            if (values[Matrix.MTRANS_X] + dx > 0)
                dx = -values[Matrix.MTRANS_X];
            else if (values[Matrix.MTRANS_X] + dx < -(mImageWidth * values[Matrix.MSCALE_X] - width))
                dx = -(mImageWidth * values[Matrix.MSCALE_X] - width) - values[Matrix.MTRANS_X];
            return dx;
        }

        /**
         * 设置缩放Matrix
         *
         * @param event
         */
        private void setZoomMatrix(MotionEvent event) {
            //只有同时触屏两个点的时候才执行
            if (event.getPointerCount() < 2) return;
            float endDis = getDistance(event);// 结束距离
            if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                float scale = endDis / mStartDis;// 得到缩放倍数
                mStartDis = endDis;//重置距离
                mCurrentMatrix.set(getImageMatrix());//初始化Matrix
                float[] values = new float[9];
                mCurrentMatrix.getValues(values);

                scale = checkMaxScale(scale, values);
                setImageMatrix(mCurrentMatrix);
            }
        }

        /**
         * 检验scale，使图像缩放后不会超出最大倍数
         *
         * @param scale
         * @param values
         * @return
         */
        private float checkMaxScale(float scale, float[] values) {
            if (scale * values[Matrix.MSCALE_X] > mMaxScale)
                scale = mMaxScale / values[Matrix.MSCALE_X];
            mCurrentMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            return scale;
        }

        /**
         * 重置Matrix
         */
        private void reSetMatrix() {
            if (checkRest()) {
                mCurrentMatrix.set(mMatrix);
                setImageMatrix(mCurrentMatrix);
            }
        }

        /**
         * 判断是否需要重置
         *
         * @return 当前缩放级别小于模板缩放级别时，重置
         */
        private boolean checkRest() {
            // TODO Auto-generated method stub
            float[] values = new float[9];
            getImageMatrix().getValues(values);
            //获取当前X轴缩放级别
            float scale = values[Matrix.MSCALE_X];
            //获取模板的X轴缩放级别，两者做比较
            mMatrix.getValues(values);
            return scale < values[Matrix.MSCALE_X];
        }

        /**
         * 判断是否支持Matrix
         */
        private void isMatrixEnable() {
            //当加载出错时，不可缩放
            if (getScaleType() != ScaleType.CENTER) {
                setScaleType(ScaleType.MATRIX);
            } else {
                mMode = MODE_UNABLE;//设置为不支持手势
            }
        }

        /**
         * 双击时触发
         */
        public void onDoubleClick() {
            float scale = isZoomChanged() ? 1 : mDobleClickScale;
            mCurrentMatrix.set(mMatrix);//初始化Matrix
            mCurrentMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            setImageMatrix(mCurrentMatrix);
//            setScale(scale);
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private final MatrixTouchListener listener;

        public GestureListener(MatrixTouchListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            //捕获Down事件
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //触发双击事件
            listener.onDoubleClick();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub
            super.onShowPress(e);
        }


        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            // TODO Auto-generated method stub
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // TODO Auto-generated method stub
            return super.onSingleTapConfirmed(e);
        }

    }


}
