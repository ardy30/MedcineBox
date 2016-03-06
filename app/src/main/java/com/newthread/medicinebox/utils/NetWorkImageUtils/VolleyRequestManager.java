package com.newthread.medicinebox.utils.NetWorkImageUtils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by 张浩 on 2016/2/29.
 */
public class VolleyRequestManager {
    public static RequestQueue mRequestQueue;

    /**
     * 初始化
     * @param context
     */
    public static void init(Context context){
        mRequestQueue= Volley.newRequestQueue(context);
    }

    /**
     * 获取queue
     * @return
     */
    public static RequestQueue getRequestQueue(){
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("Not initialized");
        }
    }
}
