package com.qyddai.an_aw_base;

import android.widget.Button;

import com.an.base.view.SuperActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by qydda on 2016/12/6.
 */


@ContentView(R.layout.activity_main)
public class AnotationActivity extends SuperActivity {
    @ViewInject(R.id.tvChangModel)
    private Button tvChangModel;

    @Override
    public void initView() {

    }
}
