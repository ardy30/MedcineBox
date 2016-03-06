package com.newthread.medicinebox.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by 张浩 on 2016/2/11.
 */
public class NetWorkHelper {
    private static String LOG_TAG = "NetWorkHelper";
    public Context context;
    public NetWorkHelper(Context context){
        this.context=context;
    }
    //获取联网信息，当前是否联网
    public boolean isOpenNetWork()
    {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=connManager.getActiveNetworkInfo();
        if(info!=null){
            return  info.isAvailable();
        }
        return false;
    }
}
