package com.newthread.medicinebox.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.newthread.medicinebox.ui.remind.AlarmAlert;

/**
 * Created by 张浩 on 2016/1/29.
 */
public class AlarmRingService extends Service {
    private String id;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        id=intent.getStringExtra("id");
        Log.d("TAG","启动了AlarmRingService");
        intent=new Intent(this,AlarmAlert.class);
        intent.putExtra("id",id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        return super.onStartCommand(intent, flags, startId);

    }

}
