package com.qyddai.an_aw_base;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.an.base.view.activity.SlideCloseActivity;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by qydda on 2016/12/6.
 */


public class AnotationActivity extends SlideCloseActivity {
    //以下为headview_standard.xml
    @ViewInject(R.id.anLlBack)
    private LinearLayout anLlBack;
    @ViewInject(R.id.anTvBack)
    private TextView anTvBack;
    @ViewInject(R.id.anPb)
    private ProgressBar anPb;
    @ViewInject(R.id.anTvTitle)
    private TextView anTvTitle;

    @ViewInject(R.id.anLlRight)
    private LinearLayout anLlRight;
    @ViewInject(R.id.anTvRight)
    private TextView anTvRight;
    @ViewInject(R.id.anIvRight)
    private ImageView anIvRight;

    //分割线0000---complex
    @ViewInject(R.id.anLlRRight)
    private LinearLayout anLlRRight;
    @ViewInject(R.id.anTvRRight)
    private TextView anTvRRight;
    @ViewInject(R.id.anIvRRight)
    private ImageView anIvRRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testdefinescrollview);
    }

    @Override
    public void onNetChange(int netModel) {
        super.onNetChange(netModel);
        if (netModel == 1) {
            showToast("连接无线网络");
        } else if (netModel == 0) {
            showToast("连接移动网络");
        } else if (netModel == -1) {
            showToast("没有连接网络");
        }
    }
}
