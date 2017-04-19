package com.an.base.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.an.base.R;
import com.an.base.utils.YbitmapUtils;
import com.an.base.utils.YscreenUtils;
import com.an.base.view.SuperActivity;
import com.an.base.view.widget.LuueSmoothIv;
import com.an.base.view.widget.LuueZoomIv;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 三级页面图片放大,可以根据传入的drawable的id，或者图片的路径地址，或者网络图片的地址来显示图片，你可以传入需要显示第几张的图片,
 * <p>
 * 应用场景是一个照片墙。
 * Created by sunshuntao on 16/4/9.
 * email: staryumou@163.com
 * 修改日期：20170418
 */
@SuppressLint("AllowBackup")
public class AnPicDetailsActivity extends SuperActivity {
    List<String> mDatas;
    private LuueSmoothIv imageView;
    private LuueZoomIv luueZoomIv;
    private String url;
    private boolean isZoom = false;//判断是否对图片缩放。
    private Bitmap bitmap;
    private int mDefaultWidth = 400;
    private int mDefaultHeight = 350;

    @Override
    public void initView() {
        setContentView(R.layout.an_activity_picdetails);
        final Intent intent = getIntent();
        imageView = new LuueSmoothIv(this);
        luueZoomIv = new LuueZoomIv(this);
        isZoom = intent.getBooleanExtra("isZoom", false);
        mDatas = new ArrayList<>();

        mDefaultWidth = YscreenUtils.getInstance(mContext).getScreenWidth();
        mDefaultHeight = YscreenUtils.getInstance(mContext).getScreenHeight();

        mDatas = intent.getStringArrayListExtra("images");
        final int mPosition = intent.getIntExtra("position", 0);
        url = intent.getStringExtra("url");
        int drawableId = intent.getIntExtra("drawableId", 0);
        String absPath = intent.getStringExtra("absPath");
        int width = intent.getIntExtra("width", mDefaultWidth);
        int height = intent.getIntExtra("height", mDefaultHeight / 2);

        if (mDatas == null) {

        } else {
            for (int i = 0; i < mDatas.size(); i++) {
                Log.d(TAG, "--yy@@--AnPicDetailsActivity--mDatas.get--" + mDatas.get(i));
            }
            url = mDatas.get(mPosition);
        }

        if (!TextUtils.isEmpty(url)) {
            if (isZoom) {
                x.image().bind(luueZoomIv, url);
            } else {
                x.image().bind(imageView, url);
            }
        }
        if (drawableId != 0) {
            bitmap = YbitmapUtils.INSTANCE.drawableToBitmap(ContextCompat.getDrawable(this, drawableId));
        }
        if (!TextUtils.isEmpty(absPath)) {
            bitmap = YbitmapUtils.INSTANCE.decodeBitmapFromPath(absPath, width, height);
            //maybe 裁剪 ，防止oom
        }

        int mLocationX = intent.getIntExtra("locationX", 0);
        int mLocationY = intent.getIntExtra("locationY", 0);
        int mWidth = intent.getIntExtra("width", mDefaultWidth / 4);
        int mHeight = intent.getIntExtra("height", mDefaultWidth / 5);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

        if (bitmap != null)
            if (isZoom) {
                luueZoomIv.setImageBitmap(bitmap);
            } else {
                imageView.setImageBitmap(bitmap);
            }
        if (isZoom) {
            onWindowTranslucentBar(android.R.color.black);
            setContentView(luueZoomIv);
        } else {
            setContentView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        }
    }


    /**
     * 退出时动画
     */
    @Override
    public void onBackPressed() {
        if (isZoom) {
            finish();
        } else {
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
}
