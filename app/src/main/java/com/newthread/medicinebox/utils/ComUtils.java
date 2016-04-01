package com.newthread.medicinebox.utils;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by 张浩 on 2016/3/2.
 */
public class ComUtils {
    public Context context;
    public ComUtils(Context context){
        this.context=context;
    }
    //检查版本
    public static boolean CheckBuildVision(){
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    public static void adjustPan(Activity activity){
                if (Build.VERSION.SDK_INT<=Build.VERSION_CODES.KITKAT){
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }

    /**
     *
     * @param context
     * @param view
     */
    public static void hideInput(Context context,View view){
        InputMethodManager inputMethodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
