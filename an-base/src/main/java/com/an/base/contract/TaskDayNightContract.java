package com.an.base.contract;

import android.content.SharedPreferences;

import com.an.base.model.entity.ResponseDayNightModel;
import com.an.base.presenter.BasePresenter;
import com.an.base.view.BaseView;

/**
 * Created by qydda on 2016/12/7.
 */

public interface TaskDayNightContract {
    interface View extends BaseView<Presenter> {
        void _initTheme();//初始化主题背景

        void initDayModel();//主题切换设置白天模式

        void initNightModel();//主题切换设置夜间模式

        void changeThemeByZhiHu();//使用知乎的套路来改变主题背景

        void refreshUI();//设置模式后刷新UI

        void showAnimation();//设置模式启动动画

    }

    interface Presenter extends BasePresenter {
        boolean isDay();//是白天模式

        boolean isNight();//是夜晚模式

        boolean setDayModel();//设置白天模式

        boolean setNightModel();//设置夜晚模式
    }

}
