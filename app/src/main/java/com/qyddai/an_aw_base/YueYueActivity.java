package com.qyddai.an_aw_base;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.an.base.view.activity.SwipeFinishActivity;
import com.an.base.view.widget.WRoundImageView;

/**
 * Created by qydda on 2017/2/21.
 */

public class YueYueActivity extends SwipeFinishActivity {
    private WRoundImageView imageView;

    @Override
    public void initView() {
        setContentView(R.layout.sst_activity_yueyue);
        imageView = (WRoundImageView) findViewById(R.id.imageView);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -100, Animation.RELATIVE_TO_SELF, 0);
        translateAnimation.setDuration(500);
        imageView.setAnimation(translateAnimation);
    }
}
