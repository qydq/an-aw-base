package com.qyddai.an_aw_base.view.activity;

import android.content.Intent;
import android.view.View;

import com.an.base.view.ParallaxActivity;
import com.an.base.view.activity.AnPicDetailsActivity;
import com.qyddai.an_aw_base.R;

import java.util.ArrayList;

/*
* isZoom 是否对图片进行放大缩小（默认false）
* images 可传递ArrayList<String>的集合-对应必须要传递需要显示的图片的position
* position 比如说ListView中的点击的位置（默认position=0）
* url 可直接传递网络上的图片的url。
* drawableId 可以传递一个drawable下面的图片的id
* absPath 可以传递一个绝对路径地址
* */
public class PicsDetailActivity extends ParallaxActivity {


    @Override
    public void initView() {
        setContentView(R.layout.sst_activity_picdetail);
    }

    public void test(View view) {
        Intent intent = new Intent(mContext, AnPicDetailsActivity.class);
//        ArrayList<String> mDatas = new ArrayList<>();
//        mDatas.add("https://www.pic.bul.com/adsfasdf.jpg");
//        intent.putStringArrayListExtra("images", mDatas);
//        intent.putExtra("position", 0);
        intent.putExtra("locationX", 100);
        intent.putExtra("locationY", 16);
//        intent.putExtra("width", 400);
//        intent.putExtra("height", 300);
        intent.putExtra("isZoom", false);
        intent.putExtra("drawableId", R.drawable.chinesefoodlock);
//        intent.putExtra("absPath", "sdcard/an_ytips/picture/shiluohua.jpg");
        startActivity(intent);
    }

    @Override
    public void onWindowTranslucentBar(int colorId) {
        super.onWindowTranslucentBar(colorId);

    }
}
