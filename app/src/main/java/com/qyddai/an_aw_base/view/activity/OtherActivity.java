package com.qyddai.an_aw_base.view.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.an.base.view.ParallaxActivity;
import com.an.base.view.activity.AnPicDetailsActivity;
import com.qyddai.an_aw_base.R;


public class OtherActivity extends ParallaxActivity {
    @Override
    public void initView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.sst_activity_other, null);
//        setContentView(R.layout.sst_activity_picdetail);
        setContentView(contentView);
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

    public void fragment(View view) {
        startActivity(new Intent(this,TakePhotoFragmentActivity.class));
    }

    public void activity(View view) {
        startActivity(new Intent(this, TakePhotoActivity.class));
    }

    @Override
    public void onWindowTranslucentBar(int colorId) {
        super.onWindowTranslucentBar(colorId);
    }
}
