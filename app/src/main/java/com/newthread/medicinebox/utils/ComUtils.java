package com.newthread.medicinebox.utils;


import android.os.Build;

/**
 * Created by 张浩 on 2016/3/2.
 */
public class ComUtils {
    public static boolean CheckBuildVision(){
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }
}
