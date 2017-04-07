package com.an.base.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;


import com.an.base.R;
import com.an.base.view.SuperActivity;
import com.an.base.view.widget.LuueSmoothIv;

import org.xutils.x;

import java.util.List;

/**
 * 三级页面图片放大
 * Created by sunshuntao on 16/4/9.
 * email: staryumou@163.com
 */
@SuppressLint("AllowBackup")
public class AnPicDetailsActivity extends SuperActivity {
    List<String> mDatas;
    private LuueSmoothIv imageView;

    @Override
    public void initView() {
        setContentView(R.layout.an_activity_picdetails);
        final Intent intent = getIntent();
        mDatas = intent.getStringArrayListExtra("images");
        final int mPosition = intent.getIntExtra("position", 0);
        for (int i = 0; i < mDatas.size(); i++) {
            System.out.println("DPicDetailsActivity,图片网址:" + mDatas.get(i));
        }

        int mLocationX = intent.getIntExtra("locationX", 0);
        int mLocationY = intent.getIntExtra("locationY", 0);
        int mWidth = intent.getIntExtra("width", 0);
        int mHeight = intent.getIntExtra("height", 0);

        imageView = new LuueSmoothIv(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        setContentView(imageView);
        x.image().bind(imageView, mDatas.get(mPosition - 1));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 退出时动画
     */
    @Override
    public void onBackPressed() {
        imageView.setOnTransformListener(new LuueSmoothIv.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == 2) {
                    finish();
                }
            }

        });
        imageView.transformOut();
    }
}
