package com.qyddai.an_aw_base.view.activity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.an.base.view.ParallaxActivity;
import com.an.base.view.activity.SwipeFinishActivity;
import com.an.base.view.ytips.pickerview.CityPickerView;
import com.an.base.view.ytips.pickerview.OptionsPickerView;
import com.an.base.view.ytips.pickerview.TimePickerView;
import com.an.base.view.ytips.pickerview.listener.OnSimpleCitySelectListener;
import com.an.base.view.ytips.selector.DigitSelector;
import com.an.base.view.ytips.selector.SexSelector;
import com.an.base.view.ytips.selector.TimeSelector;
import com.an.base.view.widget.PickerView;
import com.qyddai.an_aw_base.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by qydda on 2017/2/21.
 */

public class YYTipsActivity extends ParallaxActivity {
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

    public void showTimeiOS(View v) {
        //     TimePickerView 同样有上面设置样式的方法
        TimePickerView mTimePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);

        // 设置是否循环
        mTimePickerView.setCyclic(true);
        // 设置滚轮文字大小
//        mTimePickerView.setTextSize(TimePickerView.TextSize.SMALL);
        // 设置时间可选范围(结合 setTime 方法使用,必须在)
//        Calendar calendar = Calendar.getInstance();
//        mTimePickerView.setRange(calendar.get(Calendar.YEAR) - 100, calendar.get(Calendar.YEAR));
        // 设置选中时间
//        mTimePickerView.setTime(new Date());
        mTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                Toast.makeText(YYTipsActivity.this, format.format(date), Toast.LENGTH_SHORT).show();
            }
        });
        mTimePickerView.show();
    }

    public void showCityiOS(View v) {
        CityPickerView mCityPickerView = new CityPickerView(this);
        // 设置点击外部是否消失
//        mCityPickerView.setCancelable(true);
        // 设置滚轮字体大小
//        mCityPickerView.setTextSize(18f);
        // 设置标题
//        mCityPickerView.setTitle("我是标题");
        // 设置取消文字
//        mCityPickerView.setCancelText("我是取消文字");
        // 设置取消文字颜色
//        mCityPickerView.setCancelTextColor(Color.GRAY);
        // 设置取消文字大小
//        mCityPickerView.setCancelTextSize(14f);
        // 设置确定文字
//        mCityPickerView.setSubmitText("我是确定文字");
        // 设置确定文字颜色
//        mCityPickerView.setSubmitTextColor(Color.BLACK);
        // 设置确定文字大小
//        mCityPickerView.setSubmitTextSize(14f);
        // 设置头部背景
//        mCityPickerView.setHeadBackgroundColor(Color.RED);
        mCityPickerView.setOnCitySelectListener(new OnSimpleCitySelectListener() {
            @Override
            public void onCitySelect(String prov, String city, String area) {
                // 省、市、区 分开获取
                Log.e(TAG, "省: " + prov + " 市: " + city + " 区: " + area);
            }

            @Override
            public void onCitySelect(String str) {
                // 一起获取
                Toast.makeText(YYTipsActivity.this, "选择了：" + str, Toast.LENGTH_SHORT).show();
            }
        });
        mCityPickerView.show();
    }

    public void showSexiOS(View v) {
        OptionsPickerView<String> mOptionsPickerView = new OptionsPickerView<>(this);
        final ArrayList<String> list = new ArrayList<>();
        list.add("男");
        list.add("女");
        // 设置数据
        mOptionsPickerView.setPicker(list);
        // 设置选项单位
//        mOptionsPickerView.setLabels("性");
        mOptionsPickerView.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int option1, int option2, int option3) {
                String sex = list.get(option1);
                Toast.makeText(YYTipsActivity.this, sex, Toast.LENGTH_SHORT).show();
            }
        });
        mOptionsPickerView.show();
    }
}
