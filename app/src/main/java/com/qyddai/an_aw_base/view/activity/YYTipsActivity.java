package com.qyddai.an_aw_base.view.activity;

import android.view.View;

import com.an.base.view.activity.SwipeFinishActivity;
import com.an.base.view.tips.TimeSelector;
import com.an.base.view.widget.PickerView;
import com.qyddai.an_aw_base.R;

/**
 * Created by qydda on 2017/2/21.
 */

public class YYTipsActivity extends SwipeFinishActivity {
    private TimeSelector timeSelector;
    private PickerView month_pv;

    @Override
    public void initView() {
        setContentView(R.layout.sst_activity_tips);
        month_pv = (PickerView) findViewById(R.id.month_pv);

        timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {

            @Override

            public void handle(String time) {
                showToast(time);

            }

        }, "1989-01-30 00:00", "2017-12-31 00:00");

//        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）；
        timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        timeSelector.setTitle("选择生日");
        timeSelector.setIsLoop(true);

    }

    public void show(View v) {

        timeSelector.show();


    }
}
