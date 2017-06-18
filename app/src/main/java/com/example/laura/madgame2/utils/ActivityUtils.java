package com.example.laura.madgame2.utils;

import android.app.Activity;

/**
 * Created by Philipp on 19.04.17.
 */

public class ActivityUtils {
    private static Activity currentActivity;

    private ActivityUtils() {
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        ActivityUtils.currentActivity = currentActivity;
    }
}
