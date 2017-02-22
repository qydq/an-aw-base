package com.qyddai.an_aw_base.view.activity;

import android.view.View;

import com.an.base.view.activity.SwipeFinishActivity;
import com.an.base.view.tips.DigitSelector;
import com.an.base.view.tips.SexSelector;
import com.an.base.view.tips.TimeSelector;
import com.an.base.view.widget.PickerView;
import com.qyddai.an_aw_base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qydda on 2017/2/21.
 */

public class YYTipsActivity extends SwipeFinishActivity {
    private TimeSelector timeSelector;
    private DigitSelector digitSelector;
    private SexSelector sexSelector;
    private PickerView month_pv;

    private List<String> datas;

    @Override
    public void initView() {
        setContentView(R.layout.sst_activity_tips);
        month_pv = (PickerView) findViewById(R.id.month_pv);

        timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {

            @Override

            public void handle(String time) {
                showToast(time);

            }

        }, "1984-01-30 00:00", "2017-12-31 00:00");

//        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）；
        timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        timeSelector.setTitle("选择生日");
        timeSelector.setIsLoop(true);


        datas = new ArrayList<>();
        datas.add("男");
        datas.add("女");
        datas.add("保密");
        sexSelector = new SexSelector(this, new SexSelector.ResultHandler() {
            @Override
            public void handle(String sex) {
                showToast(sex);
            }
        }, datas);
    }

    public void showTime(View v) {
        timeSelector.show();
    }

    public void showDigitHeight(View v) {
        showPickerView("150-01-30 00:00", "210-12-31 00:00");
        digitSelector.setTitle("选择身高");
        digitSelector.setTvTips("cm");
        digitSelector.show();
    }

    public void showDigitWeight(View v) {
        showPickerView("30-01-30 00:00", "200-12-31 00:00");
        digitSelector.setTitle("选择体重");
        digitSelector.setTvTips("kg");
        digitSelector.show();
    }

    public void showDigitSex(View v) {
        sexSelector.setTitle("选择性別");
        sexSelector.show();
    }

    private void showPickerView(String startTime, String endTime) {
        digitSelector = new DigitSelector(this, new DigitSelector.ResultHandler() {
            @Override
            public void handle(int digit) {
                showToast(digit + "");
            }
        }, startTime, endTime);

        digitSelector.setMode(DigitSelector.MODE.YMD);//只显示 年月日
        digitSelector.setIsLoop(true);

    }
}
