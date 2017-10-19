package com.hey.scan.activity;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Hey on 2016/5/23.
 */

public class ActivityManager {

    private static ArrayList<Activity> activityStack;

    private static class ActivityManagerHolder {

        private static ActivityManager instance = new ActivityManager();

    }


    private ActivityManager() {
    }


    public static ActivityManager getInstance() {
        return ActivityManagerHolder.instance;
    }


    /**
     * 添加指定Activity到集合
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new ArrayList<>();
        }
        activityStack.add(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束全部的Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
