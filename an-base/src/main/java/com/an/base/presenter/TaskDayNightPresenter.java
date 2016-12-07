package com.an.base.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.an.base.contract.TaskDayNightContract;
import com.an.base.model.TaskDayNight;
import com.an.base.model.TaskDayNightImpl;
import com.an.base.model.entity.ResponseDayNightModel;

/**
 * Created by qydda on 2016/12/7.
 */

public class TaskDayNightPresenter implements TaskDayNightContract.Presenter {
    private TaskDayNightContract.View mTaskView;
    private TaskDayNight taskDayNight;

    //可以在构造函数中完成，也可以在初始化中完成。
    public TaskDayNightPresenter(SharedPreferences sp, TaskDayNightContract.View taskView) {
        this.mTaskView = taskView;
        mTaskView.setPresenter(this);
        taskDayNight = new TaskDayNightImpl(sp);
    }

    @Override
    public void start() {
        mTaskView.showAnimation();
        mTaskView.changeThemeByZhiHu();
        mTaskView.refreshUI();
    }

    @Override
    public boolean isDay() {
        return taskDayNight.isDay();
    }

    @Override
    public boolean isNight() {
        return taskDayNight.isNight();
    }

    @Override
    public boolean setDayModel() {
        return taskDayNight.setDayModel();
    }

    @Override
    public boolean setNightModel() {
        return taskDayNight.setNightMode();
    }
}
