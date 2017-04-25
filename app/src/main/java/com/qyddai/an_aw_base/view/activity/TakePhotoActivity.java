package com.qyddai.an_aw_base.view.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.an.base.model.entity.TImage;
import com.an.base.model.entity.TResult;
import com.an.base.view.activity.AnPicDetailsActivity;
import com.an.base.view.activity.PtakePhotoActivity;
import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.utils.TakePhotoHelper;

import java.util.ArrayList;

/*
* isZoom 是否对图片进行放大缩小（默认false）
* images 可传递ArrayList<String>的集合-对应必须要传递需要显示的图片的position
* position 比如说ListView中的点击的位置（默认position=0）
* url 可直接传递网络上的图片的url。
* drawableId 可以传递一个drawable下面的图片的id
* absPath 可以传递一个绝对路径地址ParallaxActivity
* */
public class TakePhotoActivity extends PtakePhotoActivity {
    private TakePhotoHelper customHelper;

    public void onClick(View view) {
        customHelper.onClick(view, getTakePhoto());
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        showImg(result.getImages());
    }

    private void showImg(ArrayList<TImage> images) {
        Intent intent = new Intent(this, TakePhotoResultActivity.class);
        intent.putExtra("images", images);
        startActivity(intent);
    }

    @Override
    public void initView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.sst_activity_picdetail, null);
//        setContentView(R.layout.sst_activity_picdetail);
        setContentView(contentView);
        customHelper = TakePhotoHelper.of(contentView);
    }

    public void test(View view) {
        Intent intent = new Intent(getApplicationContext(), AnPicDetailsActivity.class);
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
