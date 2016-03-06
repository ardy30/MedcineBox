package com.newthread.medicinebox.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.newthread.medicinebox.service.AlarmRingService;

/**
 * Created by 张浩 on 2016/1/31.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private String id;
    private String ringtone;
    @Override
    public void onReceive(Context context, Intent intent) {
        id=intent.getStringExtra("id");
        ringtone=intent.getStringExtra("ringtone");
        Log.d("TAG","启动了AlarmReceiver");
        Intent i=new Intent(context,AlarmRingService.class);
        i.putExtra("id",id);
        i.putExtra("ringtone",ringtone);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(i);
    }
}
