package com.an.base.view.widget;

/**
 * Created by qydda on 2016/11/28.
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.an.base.R;


/**
 * A custom ListView that if user sliding left a item of this ListView,
 * This item will show a Button.
 */
public class YslidingDeleteListView extends ListView {

    public static final int MAX_DISTANCE = 100;

    private int mButtonID = -1;

    private float mLastMotionX, mLastMotionY;

    private View mItemView;
    private View mButton;

    private Animation mShowAnim, mHideAnim;

    private int mLastButtonShowingPos = -1;
    private int[] mShowingButtonLocation = new int[2];

    private OnItemButtonShowingListener mDeleteItemListener;
    private boolean mCancelMotionEvent = false;
    private boolean mEnableSliding = true;

    private VelocityTracker mTracker;
    private static final int MAX_FLING_VELOCITY = ViewConfiguration.getMinimumFlingVelocity() * 10;

    public YslidingDeleteListView(Context context) {
        super(context);
        init(null);
    }

    public YslidingDeleteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public YslidingDeleteListView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if (attrs != null) {
            TypedArray styled = getContext().obtainStyledAttributes(attrs,
                    R.styleable.AnSlidingDeleteListView);

            mButtonID = styled.getResourceId(R.styleable.AnSlidingDeleteListView_anButtonID, -1);
            mEnableSliding = styled.getBoolean(
                    R.styleable.AnSlidingDeleteListView_anEnableSliding, true);

        }

        mShowAnim = AnimationUtils.loadAnimation(getContext(), R.anim.base_anim_button_show);
        mShowAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mDeleteItemListener != null)
                    mDeleteItemListener.onShowButton(mButton);

            }
        });
        mHideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.base_anim_button_hide);
        mHideAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mButton.setVisibility(View.GONE);
                if (mDeleteItemListener != null)
                    mDeleteItemListener.onHideButton(mButton);
            }
        });
    }

    /**
     * Set the resource ID of the button your want to show after sliding
     */
    public void setButtonID(int id) {
        mButtonID = id;
    }

    /**
     * Set the OnItemButtonShowingListener
     */
    public void setOnItemButtonShowingListener(OnItemButtonShowingListener listener) {
        mDeleteItemListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnableSliding)
            return false;

        if (mCancelMotionEvent && event.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        } else if (mCancelMotionEvent && event.getAction() == MotionEvent.ACTION_DOWN) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (mTracker == null)
                    mTracker = VelocityTracker.obtain();
                else
                    mTracker.clear();

                mLastMotionX = event.getX();
                mLastMotionY = event.getY();
            }
            break;

            case MotionEvent.ACTION_MOVE:
                if (mTracker == null)
                    mTracker = VelocityTracker.obtain();
                mTracker.addMovement(event);
                mTracker.computeCurrentVelocity(1000);
                int curVelocityX = (int) mTracker.getXVelocity();

                float curX = event.getX();
                float curY = event.getY();
                int lastPos = pointToPosition(
                        (int) mLastMotionX, (int) mLastMotionY);
                int curPos = pointToPosition((int) curX, (int) curY);
                int distanceX = (int) (mLastMotionX - curX);
                if (lastPos == curPos && (distanceX >= MAX_DISTANCE || curVelocityX < -MAX_FLING_VELOCITY)) {
                    int firstVisiblePos = getFirstVisiblePosition() - getHeaderViewsCount();
                    int factPos = curPos - firstVisiblePos;
                    mItemView = getChildAt(factPos);
                    if (mItemView != null) {
                        if (mButtonID == -1)
                            throw new IllegalButtonIDException("Illegal DeleteButton resource id,"
                                    + "ensure excute the function setButtonID(int id)");

                        mButton = mItemView.findViewById(mButtonID);
                        mButton.setVisibility(View.VISIBLE);
                        mButton.startAnimation(mShowAnim);

                        mLastButtonShowingPos = curPos;
                        mButton.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (mDeleteItemListener != null)
                                    mDeleteItemListener.onButtonClick(v, mLastButtonShowingPos);
                                mButton.setVisibility(View.GONE);

                                mLastButtonShowingPos = -1;
                            }
                        });
                        mCancelMotionEvent = true;
                    }
                }
                break;

            case MotionEvent.ACTION_UP: {
                if (mTracker != null) {
                    mTracker.clear();
                    mTracker.recycle();
                    mTracker = null;
                }

                mCancelMotionEvent = false;

                if (mLastButtonShowingPos != -1) {
                    event.setAction(MotionEvent.ACTION_CANCEL);
                }
            }
            break;

            case MotionEvent.ACTION_CANCEL: {
                hideShowingButtonWithAnim();
            }
            break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mEnableSliding && mLastButtonShowingPos != -1 &&
                ev.getAction() == MotionEvent.ACTION_DOWN && !isClickButton(ev)) {
            ev.setAction(MotionEvent.ACTION_CANCEL);
            mCancelMotionEvent = true;

            return true;
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * @param enable true if need enable sliding, false not
     * @set if need enable the sliding
     */
    public void setEnableSliding(boolean enable) {
        mEnableSliding = enable;
    }

    /**
     * hide the showing button without hide animation
     */
    public void hideShowingButtonWithoutAnim() {
        if (mLastButtonShowingPos != -1) {
            int firstVisiblePos = getFirstVisiblePosition()
                    - getHeaderViewsCount();
            int factPos = mLastButtonShowingPos - firstVisiblePos;
            mItemView = getChildAt(factPos);
            if (mItemView != null) {
                if (mButtonID == -1)
                    throw new IllegalButtonIDException("Illegal DeleteButton resource id,"
                            + "ensure excute the function setButtonID(int id)");

                mButton = mItemView.findViewById(mButtonID);
                mButton.setVisibility(View.INVISIBLE);
                if (mDeleteItemListener != null)
                    mDeleteItemListener.onHideButton(mButton);
            }

            mLastButtonShowingPos = -1;
        }
    }

    /**
     * hide the showing button with hide animation
     */
    public void hideShowingButtonWithAnim() {
        if (mLastButtonShowingPos != -1) {
            int firstVisiblePos = getFirstVisiblePosition()
                    - getHeaderViewsCount();
            int factPos = mLastButtonShowingPos - firstVisiblePos;
            mItemView = getChildAt(factPos);
            if (mItemView != null) {
                if (mButtonID == -1)
                    throw new IllegalButtonIDException("Illegal DeleteButton resource id,"
                            + "ensure excute the function setButtonID(int id)");

                mButton = mItemView.findViewById(mButtonID);
                mButton.startAnimation(mHideAnim);
            }

            mLastButtonShowingPos = -1;
        }
    }

    /*
     * because we must intercept the touch event when a item had showing
     * a button, so we must ensure if user just click the showing button,
     * if it's true, we not need intercept the touch event(in fact the event
     * MotionEvent.ACTION_DOWN, this touch event is very important), if false
     * we need intercept the motion event and then hide the showing button
     * */
    private boolean isClickButton(MotionEvent ev) {
        mButton.getLocationOnScreen(mShowingButtonLocation);

        int left = mShowingButtonLocation[0];
        int right = mShowingButtonLocation[0] + mButton.getWidth();
        int top = mShowingButtonLocation[1];
        int bottom = mShowingButtonLocation[1] + mButton.getHeight();

        return (ev.getRawX() >= left
                && ev.getRawX() <= right
                && ev.getRawY() >= top
                && ev.getRawY() <= bottom);
    }

    /**
     * A listener, when a item of this ListView showing a Button,
     * it will callback this listener.
     */
    public static interface OnItemButtonShowingListener {
        /**
         * after the button had played showing animation
         *
         * @param button the button which just been showing
         */
        void onShowButton(View button);

        /**
         * after the button had played disappear animation
         *
         * @param button the button which just been disappeared
         */
        void onHideButton(View button);

        /**
         * the button had been clicked
         *
         * @param button   the button which just been clicked
         * @param position the position of the item in the ListView which show this button
         */
        void onButtonClick(View button, int position);
    }

    @SuppressWarnings("serial")
    public static class IllegalButtonIDException extends RuntimeException {

        public IllegalButtonIDException() {
        }

        public IllegalButtonIDException(String msg) {
            super(msg);
        }
    }

}
