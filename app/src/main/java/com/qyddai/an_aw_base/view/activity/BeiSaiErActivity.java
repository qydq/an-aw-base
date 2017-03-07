package com.qyddai.an_aw_base.view.activity;

import com.an.base.view.SuperActivity;
import com.qyddai.an_aw_base.view.PracticeView;
import com.qyddai.an_aw_base.view.ScalView;

/**
 * Created by qydda on 2017/3/2.
 */

public class BeiSaiErActivity extends SuperActivity {
    @Override
    public void initView() {
        PracticeView scalView = new PracticeView(mContext);
        setContentView(scalView);
    }

}
