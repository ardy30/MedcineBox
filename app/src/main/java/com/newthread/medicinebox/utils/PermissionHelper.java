package com.newthread.medicinebox.utils;

import android.content.Context;

/**
 * Created by 张浩 on 2016/3/4.
 */
public class PermissionHelper {
    public static PermissionHelper mPermissionHelper;
    public static Context context;
    public PermissionHelper(Context context){
        this.context=context;
    }
    public static PermissionHelper getInstance(){
        if (mPermissionHelper==null){
            mPermissionHelper=new PermissionHelper(context);
        }
        return mPermissionHelper;
    }
}
