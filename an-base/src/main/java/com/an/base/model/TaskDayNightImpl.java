package com.an.base.model;

import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.an.base.model.entity.ResponseDayNightModel;
import com.an.base.utils.DayNightHelper;

import static android.content.ContentValues.TAG;

/**
 * Created by qydda on 2016/12/7.
 */

public class TaskDayNightImpl implements TaskDayNight {
    private DayNightHelper helper = null;

    public TaskDayNightImpl(SharedPreferences sp) {
        helper = new DayNightHelper(sp);
    }

    @Override
    public boolean setDayModel() {
        return helper.setMode(ResponseDayNightModel.DAY);
    }

    @Override
    public boolean setNightMode() {
        return helper.setMode(ResponseDayNightModel.NIGHT);
    }

    @Override
    public boolean isDay() {
        return helper.isDay();
    }

    @Override
    public boolean isNight() {
        return helper.isNight();
    }
}
