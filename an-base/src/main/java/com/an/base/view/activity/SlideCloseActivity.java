package com.an.base.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.an.base.R;
import com.an.base.view.SuperActivity;
import com.an.base.view.widget.WSlideFrameLayout;

public class SlideCloseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WSlideFrameLayout rootView = new WSlideFrameLayout(this);
        rootView.bindActivity(this);
    }

    // 覆盖以下方法,设置动画.
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.aim_common_right_in,
                R.anim.aim_common_left_out);
    }

//    @Override
//    public void startActivityForResult(Intent intent, int requestCode) {
//        super.startActivityForResult(intent, requestCode);
//        overridePendingTransition(R.anim.aim_common_right_in,
//                R.anim.aim_common_zoom_out);
//    }

//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.aim_common_left_in,
//                R.anim.aim_common_right_out);
//    }
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }
}
