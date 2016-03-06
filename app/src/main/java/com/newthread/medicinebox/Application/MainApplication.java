package com.newthread.medicinebox.Application;

import android.app.Application;
import android.util.Log;

import com.newthread.medicinebox.utils.NetWorkImageUtils.VolleyImageCacheManager;
import com.newthread.medicinebox.utils.NetWorkImageUtils.VolleyRequestManager;

import org.litepal.LitePalApplication;

import cn.bmob.v3.Bmob;

/**
 * Created by 张浩 on 2016/2/29.
 */
public class MainApplication extends LitePalApplication {
    private String AppID="3a5d16ce5d7a8c551fd43fbdbe90532d";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("create","create");
        init();
    }
    /**
     *
     */
    public void init(){
        LitePalApplication.initialize(getApplicationContext());
        Bmob.initialize(getApplicationContext(), AppID);
        VolleyRequestManager.init(getApplicationContext());
        VolleyImageCacheManager.init();
    }
}
