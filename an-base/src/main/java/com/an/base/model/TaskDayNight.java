package com.an.base.model;

import android.content.SharedPreferences;

/**
 * Created by qydda on 2016/12/7.
 */

public interface TaskDayNight {
    boolean setDayModel();

    boolean setNightMode();

    boolean isDay();

    boolean isNight();
}
