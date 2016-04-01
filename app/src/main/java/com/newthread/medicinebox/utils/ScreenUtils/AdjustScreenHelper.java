package com.newthread.medicinebox.utils.ScreenUtils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by 张浩 on 2016/3/25.
 */
public class AdjustScreenHelper {
    public DisplayMetrics metrics;
    public Activity activity;
    public AdjustScreenHelper(Activity activity){
        this.activity=activity;
        metrics=new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
    }
    public float getYdpi(){
        return metrics.ydpi;
    }
    public float getScreenDensity(){
        return metrics.density;//获取基准比例
    }
    public int getScreenWidthPixels(){
        return metrics.widthPixels;//获取屏幕宽度
    }
    public float getScreenHeight(){
        return metrics.heightPixels+32*metrics.ydpi/160;//获取屏幕高度

    }
    /**
     * dp转换为px
     * @param dp
     * @return
     */
    public float dp2px(float dp){
       return dp*getScreenDensity()+0.5f;
    }

    /**
     * px转换为dp
     * @param px
     * @return
     */
    public float px2dp(float px){
        return px/getScreenDensity()+0.5f;
    }


}
