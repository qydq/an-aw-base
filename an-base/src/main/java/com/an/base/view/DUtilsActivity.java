package com.an.base.view;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Lyue on 2016/8/11.
 * Email : staryumou@163.com
 */

public class DUtilsActivity extends Application {
    private List<Activity> mList = new LinkedList();
    private static DUtilsActivity instance;

    private DUtilsActivity() {
    }

    public synchronized static DUtilsActivity getInstance() {
        if (null == instance) {
            instance = new DUtilsActivity();
        }
        return instance;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    /**
     * @param activity
     */
    public void removeActivity(Activity activity) {
        mList.remove(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
